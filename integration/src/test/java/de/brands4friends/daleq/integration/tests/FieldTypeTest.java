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

package de.brands4friends.daleq.integration.tests;

import static de.brands4friends.daleq.core.Daleq.aRow;
import static de.brands4friends.daleq.core.Daleq.aTable;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import de.brands4friends.daleq.core.DaleqException;
import de.brands4friends.daleq.core.FieldType;
import de.brands4friends.daleq.core.Table;
import de.brands4friends.daleq.core.TableType;
import de.brands4friends.daleq.core.internal.builder.SimpleContext;
import de.brands4friends.daleq.core.internal.template.TemplateValueFactory;
import de.brands4friends.daleq.integration.beans.TableProvider;


public class FieldTypeTest extends BaseTest {

    public static final long HUGE_LONG_VAL = 1273651726351726351L;

    @Autowired
    private TableProvider tableProvider;

    private TableType tableType;
    private TemplateValueFactory templateValueFactory;

    @Before
    public void setUp() {
        final SimpleContext context = new SimpleContext();
        templateValueFactory = context.getService(TemplateValueFactory.class);
        tableType = aTable(tableProvider.allTypesTable()).build(context).getTableType();
    }

    @Test
    public void insert_hugeLong_into_AllTypes() {
        assertInsertValueInAllFields(HUGE_LONG_VAL);
    }

    @Test
    public void insert_zero_into_AllTypes() {
        assertInsertValueInAllFields(0);
    }

    @Test
    public void insert_127_into_AllTypes() {
        assertInsertValueInAllFields(127);
    }

    @Test
    public void insert_128_into_AllTypes() {
        assertInsertValueInAllFields(128);
    }

    @Test
    public void insert_255_into_AllTypes() {
        assertInsertValueInAllFields(255);
    }

    @Test
    public void insert_32767_into_AllTypes() {
        assertInsertValueInAllFields(32767);
    }

    @Test
    public void insert_32768_into_AllTypes() {
        assertInsertValueInAllFields(32768);
    }

    @Test
    public void insert_65535_into_AllTypes() {
        assertInsertValueInAllFields(65535);
    }

    @Test
    public void insert_65536_into_AllTypes() {
        assertInsertValueInAllFields(65536);
    }

    @Test
    public void insert_2147483647_into_AllTypes() {
        assertInsertValueInAllFields(2147483647L);
    }

    @Test
    public void insert_2147483648_into_AllTypes() {
        assertInsertValueInAllFields(2147483648L);
    }

    @Test
    public void insert_maxLong_into_AllTypes() {
        assertInsertValueInAllFields(Long.MAX_VALUE);
    }

    @Test
    public void insert_minus1_into_AllTypes() {
        assertInsertValueInAllFields(-1);
    }

    @Test
    public void insert_minus127_into_AllTypes() {
        assertInsertValueInAllFields(-127);
    }

    @Test
    public void insert_minus128_into_AllTypes() {
        assertInsertValueInAllFields(-128);
    }

    @Test
    public void insert_minus255_into_AllTypes() {
        assertInsertValueInAllFields(-255);
    }

    @Test
    public void insert_minus32767_into_AllTypes() {
        assertInsertValueInAllFields(-255);
    }

    @Test
    public void insert_minus32768_into_AllTypes() {
        assertInsertValueInAllFields(-255);
    }

    @Test
    public void insert_minus65535_into_AllTypes() {
        assertInsertValueInAllFields(-65535);
    }

    @Test
    public void insert_minus65536_into_AllTypes() {
        assertInsertValueInAllFields(-65536);
    }

    @Test
    public void insert_minus2147483647_into_AllTypes() {
        assertInsertValueInAllFields(-2147483647L);
    }

    @Test
    public void insert_minus2147483648_into_AllTypes() {
        assertInsertValueInAllFields(-2147483648L);
    }

    @Test
    public void insert_minLong_into_AllTypes() {
        assertInsertValueInAllFields(Long.MIN_VALUE);
    }


    private void assertInsertValueInAllFields(final long value) {
        final List<String> errors = Lists.newArrayList();
        for (FieldType fieldType : tableType.getFields()) {
            final String templatized = templatizeValue(fieldType, value);
            final Table table = aTable(tableProvider.allTypesTable())
                    .with(aRow(1L).f(fieldType.getOrigin(), templatized));

            try {
                daleq.insertIntoDatabase(table);
            } catch (DaleqException e) {
                final String name = fieldType.getName();
                final String msg = "Could not insert " + name + " with templatized value " + templatized
                        + " (derived from " + value + "), because " + e.getMessage();
                errors.add(msg);
            }
        }

        if (!errors.isEmpty()) {
            Assert.fail("Could not insert due to the following reasons:\n" + Joiner.on("\n").join(errors));
        }

    }

    private String templatizeValue(final FieldType fieldType, final long value) {
        return templateValueFactory.create(fieldType.getDataType(), fieldType.getName()).render(value);
    }
}
