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

package de.brands4friends.daleq.core.internal.template;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.brands4friends.daleq.core.DataType;

public class TemplateValueFactoryImplTest {

    private TemplateValueFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = TemplateValueFactoryImpl.getInstance();
    }

    @Test
    public void defaultTemplate_of_VARCHAR() {
        assertStringFieldRendering(DataType.VARCHAR);
    }

    @Test
    public void defaultTemplate_of_CHAR() {
        assertCharFieldRendering();
    }

    @Test
    public void defaultTemplate_of_LONGVARCHAR() {
        assertStringFieldRendering(DataType.LONGVARCHAR);
    }

    @Test
    public void defaultTemplate_of_NCHAR() {
        assertCharFieldRendering();
    }

    @Test
    public void defaultTemplate_of_NVARCHAR() {
        assertStringFieldRendering(DataType.NVARCHAR);
    }

    @Test
    public void defaultTemplate_of_LONGNVARCHAR() {
        assertStringFieldRendering(DataType.LONGNVARCHAR);
    }

    @Test
    public void defaultTemplate_of_CLOB() {
        assertStringFieldRendering(DataType.CLOB);
    }

    @Test
    public void defaultTemplate_of_NUMERIC() {
        assertNumericFieldRendering(DataType.NUMERIC);
    }

    private void assertNumericFieldRendering(final DataType dataType) {
        assertRendering(dataType, "13");
    }

    @Test
    public void defaultTemplate_of_DECIMAL() {
        assertNumericFieldRendering(DataType.DECIMAL);
    }

    @Test
    public void defaultTemplate_of_BOOLEAN() {
        assertRendering(DataType.BOOLEAN, "1");
    }

    @Test
    public void defaultTemplate_of_BIT() {
        assertRendering(DataType.BIT, "1");
    }

    @Test
    public void defaultTemplate_of_INTEGER() {
        assertNumericFieldRendering(DataType.INTEGER);
    }

    @Test
    public void defaultTemplate_of_TINYINT() {
        assertNumericFieldRendering(DataType.TINYINT);
    }

    @Test
    public void defaultTemplate_of_SMALLINT() {
        assertNumericFieldRendering(DataType.SMALLINT);
    }

    @Test
    public void defaultTemplate_of_BIGINT() {
        assertNumericFieldRendering(DataType.BIGINT);
    }

    @Test
    public void defaultTemplate_of_REAL() {
        assertNumericFieldRendering(DataType.REAL);
    }

    @Test
    public void defaultTemplate_of_DOUBLE() {
        assertNumericFieldRendering(DataType.DOUBLE);
    }

    @Test
    public void defaultTemplate_of_FLOAT() {
        assertNumericFieldRendering(DataType.FLOAT);
    }

    @Test
    public void defaultTemplate_of_DATE() {
        assertRendering(DataType.DATE, "1970-01-14");
    }

    @Test
    public void defaultTemplate_of_TIME() {
        assertRendering(DataType.TIME, "1970-01-01 01:00:14.000");
    }

    @Test
    public void defaultTemplate_of_TIMESTAMP() {
        assertRendering(DataType.TIME, "1970-01-01 01:00:14.000");
    }

    @Test
    public void defaultTemplate_of_VARBINARY() {
        assertBase64FieldRendering();
    }

    @Test
    public void defaultTemplate_of_BINARY() {
        assertBase64FieldRendering();
    }

    @Test
    public void defaultTemplate_of_LONGVARBINARY() {
        assertBase64FieldRendering();
    }

    @Test
    public void defaultTemplate_of_BLOB() {
        assertBase64FieldRendering();
    }

    @Test
    public void defaultTemplate_of_BIGINT_AUX_LONG() {
        assertNumericFieldRendering(DataType.BIGINT_AUX_LONG);
    }

    private void assertStringFieldRendering(final DataType dataType) {
        assertRendering(dataType, "theField-13");
    }

    private void assertRendering(final DataType dataType, final String expected) {
        assertThat(factory.create(dataType, "theField").render(13L), is(expected));
    }

    private void assertBase64FieldRendering() {
        assertRendering(DataType.VARBINARY, "AAAAAAAAAA0=");
    }

    private void assertCharFieldRendering() {
        assertRendering(DataType.CHAR, "N");
    }

}
