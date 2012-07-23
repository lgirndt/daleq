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

package de.brands4friends.daleq.core.internal.builder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import de.brands4friends.daleq.core.Daleq;
import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.FieldType;
import de.brands4friends.daleq.core.FieldTypeReference;
import de.brands4friends.daleq.core.FkConstraint;
import de.brands4friends.daleq.core.TableDef;
import de.brands4friends.daleq.core.TableType;
import de.brands4friends.daleq.core.TemplateValue;
import de.brands4friends.daleq.core.internal.template.StringTemplateValue;
import de.brands4friends.daleq.core.internal.types.ClassBasedTableTypeReference;
import de.brands4friends.daleq.core.internal.types.FkConstraintImpl;
import de.brands4friends.daleq.core.internal.types.TableTypeFactory;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class FieldDefBuilderTest {

    public static final String NAME = "foo";
    public static final String TEMPLATE = "foo";
    public static final String NEW_NAME = "new name";
    private TableTypeFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new TableTypeFactory();
    }

    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier
                .forClass(FieldDefBuilder.class)
                .withPrefabValues(Optional.class, Optional.of("a"), Optional.of("b"))
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void aFieldWithName_should_haveName() {
        assertThat(Daleq.fd(DataType.BIGINT).name(NAME).getName().isPresent(), is(true));
    }

    @Test
    public void aFieldWithoutName_should_haveName() {
        assertThat(Daleq.fd(DataType.BIGINT).getName().isPresent(), is(false));
    }

    @Test
    public void aFieldWithTemplate_should_haveATemplate() {
        assertThat(someFd().template(TEMPLATE).getTemplate().isPresent(), is(true));
    }

    @Test
    public void aFieldWithoutTemplate_should_haveNoTemplate() {
        assertThat(someFd().getTemplate().isPresent(), is(false));
    }

    @Test
    public void aFieldTemplate_should_beCorrect() {
        final TemplateValue expected = new StringTemplateValue(TEMPLATE);
        assertThat(someFd().template(TEMPLATE).getTemplate().get(), is(expected));
    }

    @Test
    public void chainNameAndTemplate_should_haveBoth() {
        final FieldDef fd = someFd().name(NEW_NAME).template(TEMPLATE);
        assertThat(fd.getName().get(), is(NEW_NAME));
        final TemplateValue expected = new StringTemplateValue(TEMPLATE);
        assertThat(fd.getTemplate().get(), is(expected));
    }

    @Test
    public void chainTemplateAndName_should_haveBoth() {
        final FieldDef fd = someFd().template(TEMPLATE).name(NEW_NAME);
        assertThat(fd.getName().get(), is(NEW_NAME));
        final TemplateValue expected = new StringTemplateValue(TEMPLATE);
        assertThat(fd.getTemplate().get(), is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void nameWithNull_should_fail() {
        final FieldDef fd = someFd().name(null);
        // should already have failed!
        assertThat(fd, Matchers.is(nullValue()));
    }

    @Test(expected = NullPointerException.class)
    public void templateWithNull_should_fail() {
        final FieldDef fd = someFd().template(null);
        // should already have failed!
        assertThat(fd, Matchers.is(nullValue()));
    }


    @TableDef("REFERENCED")
    public static class ReferencedTable {
        public static final FieldDef REF = Daleq.fd(DataType.INTEGER);
    }

    @Test
    public void fkConstraint_should_beCorrect() {
        final FieldDef fd = someFd().fkConstraint(ReferencedTable.class, ReferencedTable.REF);
        final FkConstraint expected = new FkConstraintImpl(
                ClassBasedTableTypeReference.of(ReferencedTable.class),
                ReferencedTable.REF
        );
        assertThat(fd.getFkConstraint().get(), is(expected)
        );
    }

    @Test(expected = NullPointerException.class)
    public void nullTableOnFkConstraint_should_throw() {
        someFd().fkConstraint(null, ReferencedTable.REF);
    }

    @Test(expected = NullPointerException.class)
    public void nullFieldOnFkConstraint_should_fail() {
        someFd().fkConstraint(ReferencedTable.class, null);
    }

    @TableDef("RESOLVE")
    public static class ResolveTable {
        public static final FieldDef ID = Daleq.fd(DataType.BIGINT);
    }

    @Test
    public void resolve_should_mapFieldDefToExistingType() {
        final TableType tableType = factory.create(ResolveTable.class);
        final FieldType fieldType = ResolveTable.ID.resolve(tableType);
        assertThat(fieldType.getOrigin(), is((FieldTypeReference) ResolveTable.ID));
    }

    @Test
    public void resolve_should_returnNullForUnmappedType() {
        final TableType tableType = factory.create(ResolveTable.class);
        final FieldDef fd = Daleq.fd(DataType.BIGINT);
        final FieldType fieldType = fd.resolve(tableType);
        assertThat(fieldType, is(nullValue()));
    }

    private FieldDef someFd() {
        return Daleq.fd(DataType.INTEGER);
    }
}
