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

package de.brands4friends.daleq.examples.slides;

import static de.brands4friends.daleq.core.Daleq.aRow;
import static de.brands4friends.daleq.core.Daleq.aTable;
import static de.brands4friends.daleq.examples.ProductTable.NAME;
import static de.brands4friends.daleq.examples.ProductTable.PRICE;
import static de.brands4friends.daleq.examples.ProductTable.SIZE;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.brands4friends.daleq.core.DaleqSupport;
import de.brands4friends.daleq.core.Table;
import de.brands4friends.daleq.examples.ProductTable;
import de.brands4friends.daleq.examples.TestConfig;

/**
 * This is example code, we use in slides.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SlidesTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DaleqSupport daleq;

    @Test
    public void singleRow() throws IOException {
        final Table table =
                aTable(ProductTable.class).with(
                        aRow(1)
                );
        daleq.printTable(table, System.out);
    }

    @Test
    public void fields() throws IOException {
        final Table table =
                aTable(ProductTable.class).with(
                        aRow(1)
                                .f(NAME, "Red Shirt")
                                .f(SIZE, "XL")
                                .f(PRICE, "10.99")
                );
        daleq.printTable(table, System.out);
    }

    @Test
    public void moreRows() throws IOException {
        final Table table =
                aTable(ProductTable.class).with(
                        aRow(1), aRow(2), aRow(3)
                );
        daleq.printTable(table, System.out);
    }

    @Test
    public void rowsBetween() throws IOException {
        final Table table =
                aTable(ProductTable.class)
                        .withRowsBetween(1, 5);

        daleq.printTable(table, System.out);
    }

    @Test
    public void rowsBetweenAndWith() throws IOException {
        final Table table =
                aTable(ProductTable.class)
                        .withRowsBetween(1, 4)
                        .with(aRow(5).f(SIZE, "M"));

        daleq.printTable(table, System.out);
    }

    @Test
    public void having() throws IOException {
        final Table table =
                aTable(ProductTable.class)
                        .withRowsBetween(1, 5)
                        .having(SIZE, "S", "M", "L", "XL");

        daleq.printTable(table, System.out);
    }

    @Test
    public void allHaving() throws IOException {
        final Table table =
                aTable(ProductTable.class)
                        .withRowsBetween(1, 4)
                        .allHaving(SIZE, "M");

        daleq.printTable(table, System.out);
    }

    @Test
    public void chained() throws IOException {
        final Table table =
                aTable(ProductTable.class)
                        .withRowsBetween(1, 4)
                        .allHaving(SIZE, "M")
                        .with(aRow(5).f(NAME, "foo"));

        daleq.printTable(table, System.out);
    }

}
