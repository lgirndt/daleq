/*
 * Copyright 2012 brands4friends, Private Sale GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.brands4friends.daleq.examples;

import static de.brands4friends.daleq.core.Daleq.aRow;
import static de.brands4friends.daleq.core.Daleq.aTable;
import static de.brands4friends.daleq.examples.OrderItemTable.ORDER_ID;
import static de.brands4friends.daleq.examples.OrderItemTable.PRODUCT_ID;
import static de.brands4friends.daleq.examples.OrderTable.CREATION;
import static de.brands4friends.daleq.examples.OrderTable.CUSTOMER_ID;
import static de.brands4friends.daleq.examples.ProductTable.PRICE;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.brands4friends.daleq.core.DaleqSupport;
import de.brands4friends.daleq.core.Table;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcOrderDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    public static final int PRODUCT_1 = 1000;
    public static final int PRODUCT_10 = 1001;
    public static final int PRODUCT_9_99 = 1002;
    public static final int PRODUCT_50 = 1003;

    public static final int CUSTOMER_1 = 23001;
    public static final int CUSTOMER_2 = 23002;
    public static final int CUSTOMER_3 = 23003;

    @Autowired
    private DaleqSupport daleq;

    private DataSource dataSource;
    private JdbcOrderDao orderDao;

    private LocalDate creationDay;
    private DateTime duringCreationDay;
    private BigDecimal fortyEuro;

    @Override
    @Autowired
    public void setDataSource(final DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

    @Before
    public void setUp() {

        this.orderDao = new JdbcOrderDao();
        this.orderDao.setDataSource(dataSource);

        final Table customers = aTable(CustomerTable.class).with(
                aRow(CUSTOMER_1),
                aRow(CUSTOMER_2),
                aRow(CUSTOMER_3)
        );
        final Table products = aTable(ProductTable.class).with(
                aRow(PRODUCT_1).f(PRICE, "1.00"),
                aRow(PRODUCT_10).f(PRICE, "10.00"),
                aRow(PRODUCT_9_99).f(PRICE, "9.99"),
                aRow(PRODUCT_50).f(PRICE, "50.00")
        );
        daleq.insertIntoDatabase(customers, products);

        creationDay = new LocalDate(2012, 8, 1);
        duringCreationDay = creationDay.toDateTime(new LocalTime(10, 30));
        fortyEuro = BigDecimal.valueOf(400, 2);
    }

    @Test
    public void findExpensiveOrders_should_selectOrdersOfThatUser() {
        final Table orders = aTable(OrderTable.class)
                .withRowsBetween(1, 10)
                .having(CUSTOMER_ID,
                        CUSTOMER_1, CUSTOMER_1, CUSTOMER_1,
                        CUSTOMER_2, CUSTOMER_2, CUSTOMER_2,
                        CUSTOMER_3, CUSTOMER_3, CUSTOMER_3, CUSTOMER_3
                )
                .allHaving(CREATION, duringCreationDay);

        final Table orderItems = aTable(OrderItemTable.class)
                .withRowsBetween(1, 10)
                .having(ORDER_ID, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .allHaving(PRODUCT_ID, PRODUCT_50);

        daleq.insertIntoDatabase(orders, orderItems);

        final List<Order> actual = orderDao.findExpensiveOrders(CUSTOMER_1, creationDay, fortyEuro);
        assertContainsOrders(actual, order(1), order(2), order(3));
    }

    @Test
    public void findExpensiveOrders_should_selectOrdersOnCreationDay() {

        final DateTime beginOfCreationDay = creationDay.toDateTimeAtStartOfDay();

        final Table orders = aTable(OrderTable.class).with(
                aRow(1).f(CREATION, beginOfCreationDay.minusDays(10)),
                aRow(2).f(CREATION, beginOfCreationDay.minusDays(1)),
                aRow(3).f(CREATION, beginOfCreationDay.minusSeconds(1)),
                aRow(4).f(CREATION, beginOfCreationDay),
                aRow(5).f(CREATION, beginOfCreationDay.plusSeconds(1)),
                aRow(6).f(CREATION, beginOfCreationDay.plusHours(5)),
                aRow(7).f(CREATION, creationDay.toDateTime(new LocalTime(23, 59, 59))),
                aRow(8).f(CREATION, beginOfCreationDay.plusDays(1)),
                aRow(9).f(CREATION, beginOfCreationDay.plusDays(10))
        ).allHaving(CUSTOMER_ID, CUSTOMER_1);

        final Table orderItems = aTable(OrderItemTable.class)
                .withRowsBetween(1, 9)
                .having(ORDER_ID, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .allHaving(PRODUCT_ID, PRODUCT_50);

        daleq.insertIntoDatabase(orders, orderItems);

        final List<Order> actual = orderDao.findExpensiveOrders(CUSTOMER_1, creationDay, fortyEuro);
        assertContainsOrders(actual, order(4), order(5), order(6), order(7));
    }

    @Test
    public void findExpensiveOrders_should_selectCorrectlySummedUpItems() throws IOException {

        final Table orders = aTable(OrderTable.class)
                .withRowsBetween(1, 10)
                .allHaving(CUSTOMER_ID, CUSTOMER_1)
                .allHaving(CREATION, duringCreationDay);

        final Table orderItems = aTable(OrderItemTable.class)
                // let orders 1-7 have just too cheap products
                .withRowsBetween(101, 107)
                .having(ORDER_ID, 1, 2, 3, 4, 5, 6, 7)
                .allHaving(PRODUCT_ID, PRODUCT_1)
                .with(
                        // Order 8: Just not enough
                        aRow(108).f(ORDER_ID, 8).f(PRODUCT_ID, PRODUCT_10),
                        aRow(109).f(ORDER_ID, 8).f(PRODUCT_ID, PRODUCT_10),
                        aRow(110).f(ORDER_ID, 8).f(PRODUCT_ID, PRODUCT_10),
                        aRow(111).f(ORDER_ID, 8).f(PRODUCT_ID, PRODUCT_9_99),

                        // Order 9: Exactly 40.00
                        aRow(112).f(ORDER_ID, 9).f(PRODUCT_ID, PRODUCT_10),
                        aRow(113).f(ORDER_ID, 9).f(PRODUCT_ID, PRODUCT_10),
                        aRow(114).f(ORDER_ID, 9).f(PRODUCT_ID, PRODUCT_10),
                        aRow(115).f(ORDER_ID, 9).f(PRODUCT_ID, PRODUCT_10),

                        // Order 10: > 40.00
                        aRow(116).f(ORDER_ID, 10).f(PRODUCT_ID, PRODUCT_50)
                );

        daleq.insertIntoDatabase(orders, orderItems);

        final List<Order> actual = orderDao.findExpensiveOrders(CUSTOMER_1, creationDay, fortyEuro);
        assertContainsOrders(actual, order(8), order(9), order(10));
    }

    @SuppressWarnings("unchecked")
    private void assertContainsOrders(final List<Order> actual, final Matcher... expected) {
        assertThat(actual, containsInAnyOrder(expected));
    }

    private Matcher order(final long id) {
        return new BaseMatcher<Order>() {
            @Override
            public boolean matches(final Object other) {
                if (!(other instanceof Order)) {
                    return false;
                }
                final Order order = (Order) other;
                return id == order.getId();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("order with id ").appendValue(id);
            }
        };
    }
}
