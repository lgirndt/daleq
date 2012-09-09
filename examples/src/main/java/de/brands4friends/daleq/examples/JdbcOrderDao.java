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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class JdbcOrderDao extends JdbcDaoSupport implements OrderDao {

    public static final RowMapper<Order> ROW_MAPPER = new RowMapper<Order>() {
        @Override
        public Order mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Order(
                    rs.getLong("ID"),
                    rs.getLong("CUSTOMER_ID"),
                    new DateTime(rs.getTimestamp("CREATION"))
            );
        }
    };

    @Override
    public List<Order> findExpensiveOrders(
            final long customerId,
            final LocalDate boughtAt,
            final BigDecimal minimumAmount) {
        final DateTime dateLower = boughtAt.toDateMidnight().toDateTime();
        final DateTime dateUpper = dateLower.plusDays(1);
        return getJdbcTemplate().query(
                "select o.* from CUSTOMER_ORDER o " +
                        "LEFT JOIN (" +
                        "   SELECT o.ID as ORDER_ID,SUM(PRICE) as TOTAL" +
                        "   FROM CUSTOMER_ORDER o " +
                        "       LEFT JOIN CUSTOMER_ORDER_ITEM oi ON o.ID = oi.ORDER_ID " +
                        "       LEFT JOIN PRODUCT p ON oi.PRODUCT_ID = p.ID " +
                        "   GROUP BY o.ID" +
                        ") prices ON o.ID = prices.ORDER_ID " +
                        "WHERE o.CUSTOMER_ID = ? " +
                        "      AND o.CREATION >= ? " +
                        "      AND o.CREATION < ? " +
                        "      AND prices.TOTAL >= ? ",
                ROW_MAPPER,
                customerId, dateLower.toDate(), dateUpper.toDate(), minimumAmount
        );
    }
}
