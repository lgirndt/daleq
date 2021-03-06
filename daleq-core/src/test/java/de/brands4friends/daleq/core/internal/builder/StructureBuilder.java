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

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import de.brands4friends.daleq.core.FieldData;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.FieldType;
import de.brands4friends.daleq.core.RowData;
import de.brands4friends.daleq.core.TableData;
import de.brands4friends.daleq.core.TableType;

public class StructureBuilder {

    public static final class PropertyContainerBean {

        private final FieldDef fieldDef;
        private final String value;

        private PropertyContainerBean(final FieldDef fieldDef, final String value) {
            this.fieldDef = fieldDef;
            this.value = value;
        }

    }

    private final TableType tableType;

    public StructureBuilder(final TableType tableType) {
        this.tableType = tableType;
    }

    public TableData table(final RowData... rows) {
        return new ImmutableTableData(tableType, Arrays.asList(rows));
    }

    public RowData row(final PropertyContainerBean... props) {
        return new ImmutableRowData(Lists.transform(
                Arrays.asList(props),
                new Function<PropertyContainerBean, FieldData>() {
                    @Override
                    public FieldData apply(@Nullable final PropertyContainerBean input) {
                        if (input == null) {
                            throw new IllegalArgumentException("input");
                        }
                        final FieldType fieldType = tableType.findFieldBy(input.fieldDef);
                        return new ImmutableFieldData(fieldType.getName(), input.value);
                    }
                }));
    }

    @SuppressWarnings("PMD.AccessorClassGeneration") // its the intention of this test helper to act like a factory.
    // if this method would be static, it would also be awkward.
    public PropertyContainerBean field(final FieldDef fieldDef, final String value) {
        return new PropertyContainerBean(fieldDef, value);
    }
}
