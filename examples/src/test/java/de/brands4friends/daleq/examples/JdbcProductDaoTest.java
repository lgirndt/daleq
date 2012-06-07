package de.brands4friends.daleq.examples;

import static de.brands4friends.daleq.Daleq.aRow;
import static de.brands4friends.daleq.Daleq.aTable;
import static de.brands4friends.daleq.examples.ProductTable.ID;
import static de.brands4friends.daleq.examples.ProductTable.SIZE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;

import de.brands4friends.daleq.Table;
import de.brands4friends.daleq.jdbc.DaleqSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcProductDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    public static final Function<Product,Long> TO_ID = new Function<Product, Long>() {
        @Override
        public Long apply(final Product input) {
            return input.getId();
        }
    };

    @Autowired
    private DaleqSupport daleq;

    private DataSource dataSource;
    private JdbcProductDao productDao;

    @Override @Autowired
    public void setDataSource(final DataSource dataSource) {
        super.setDataSource(dataSource);
        this.productDao = new JdbcProductDao(dataSource);
    }

    @Test
    public void findById_should_returnExistingProduct() {

        final long expectedId = 42l;

        final Table table = aTable(ProductTable.class).with(
                aRow(11).f(ID, expectedId)
        );

        daleq.insertIntoDatabase(table);

        final Product product = productDao.findById(expectedId);
        assertThat(product.getId(), is(expectedId));
    }

    @Test
    public void findBySize_should_returnThoseProductsHavingThatSize(){
        daleq.insertIntoDatabase(
                aTable(ProductTable.class)
                    .withRowsUntil(10)
                    .with(
                            aRow(10).f(SIZE, "S"),
                            aRow(11).f(SIZE, "S"),
                            aRow(12).f(SIZE, "M"),
                            aRow(13).f(SIZE, "L")
                    )
        );
        final List<Product> products = productDao.findBySize("S");

        assertProductsWithIds(products, 10l,11l);
    }

    private void assertProductsWithIds(final List<Product> products, final long ... expectedIds) {
        Set<Long> ids = Sets.newHashSet(Iterables.transform(products, TO_ID));
        Set<Long> expected = Sets.newHashSet(Longs.asList(expectedIds));
        assertThat(ids, is(expected));
    }
}
