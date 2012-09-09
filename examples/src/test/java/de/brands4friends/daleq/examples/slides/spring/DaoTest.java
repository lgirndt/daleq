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

package de.brands4friends.daleq.examples.slides.spring;

import java.util.List;

import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.brands4friends.daleq.examples.JdbcProductDao;
import de.brands4friends.daleq.examples.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class DaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    private JdbcProductDao jdbcProductDao;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        super.setDataSource(dataSource);
        this.jdbcProductDao = new JdbcProductDao(dataSource);
    }

    @Test
    public void testQuery() {
        // How do setup data?
        final List<Product> found = jdbcProductDao.findBySize("M");
        // And how to assert something?
        Assert.assertThat(found, Matchers.is(Matchers.not(Matchers.nullValue())));
    }
}
