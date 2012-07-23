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

package de.brands4friends.daleq.core.internal.types;

import static de.brands4friends.daleq.core.Daleq.fd;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.brands4friends.daleq.core.Daleq;
import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.FieldType;
import de.brands4friends.daleq.core.FkConstraint;
import de.brands4friends.daleq.core.TemplateValue;
import de.brands4friends.daleq.core.internal.template.StringTemplateValue;

public class FieldScannerTest {

    public static final String NAME = "some name";
    private FieldScanner scanner;

    @Before
    public void setUp() throws Exception {
        scanner = new FieldScanner();
    }

    static class WithoutPropertyDefs {

    }

    @Test(expected = IllegalArgumentException.class)
    public void scanningAClassWithoutAPropertyDef_should_fail() {
        scanner.scan(WithoutPropertyDefs.class);
    }

    static class WithNonStatic {
        public final FieldDef xID = Daleq.fd(DataType.INTEGER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scanningWithNonStatic_should_fail() {
        scanner.scan(WithNonStatic.class);
    }

    static class WithNonFinal {
        public static FieldDef xID = Daleq.fd(DataType.INTEGER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scanningWithNonFinal_should_fail() {
        scanner.scan(WithNonFinal.class);
    }

    static class WithPropertyDefs {
        public static final FieldDef ID = Daleq.fd(DataType.INTEGER);
        public static final FieldDef NAME = Daleq.fd(DataType.VARCHAR);
    }

    @Test
    public void scanningAClassWithPropertyDefs_should_extractThosePropertyDefs() {
        final Collection<FieldType> expected = Lists.<FieldType>newArrayList(
                new FieldTypeImpl(
                        "ID",
                        DataType.INTEGER,
                        Optional.<TemplateValue>absent(),
                        WithPropertyDefs.ID, Optional.<FkConstraint>absent()),
                new FieldTypeImpl(
                        "NAME",
                        DataType.VARCHAR,
                        Optional.<TemplateValue>absent(),
                        WithPropertyDefs.NAME, Optional.<FkConstraint>absent())
        );
        assertThat(scanner.scan(WithPropertyDefs.class), is(expected));
    }

    static class WithExplicitName {
        public static final FieldDef ID = fd(DataType.INTEGER).name(NAME);
    }

    @Test
    public void scanningWithExplicitName_should_haveThatName() {
        final Collection<FieldType> expected = Lists.<FieldType>newArrayList(
                new FieldTypeImpl(
                        NAME,
                        DataType.INTEGER,
                        Optional.<TemplateValue>absent(),
                        WithExplicitName.ID,
                        Optional.<FkConstraint>absent())
        );
        assertThat(scanner.scan(WithExplicitName.class), is(expected));
    }

    static class WithExplicitTemplate {
        public static final FieldDef NAME = fd(DataType.VARCHAR).template("some template");
    }

    @Test
    public void scanningWithExplicitTemplate_should_haveTheTemplate() {
        assertThat(
                scanner.scan(WithExplicitTemplate.class),
                contains(
                        (FieldType) new FieldTypeImpl(
                                "NAME",
                                DataType.VARCHAR,
                                Optional.<TemplateValue>of(new StringTemplateValue("some template")),
                                WithExplicitTemplate.NAME,
                                Optional.<FkConstraint>absent())
                )
        );
    }
}
