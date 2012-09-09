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

import static com.google.common.collect.ImmutableSet.of;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.brands4friends.daleq.core.DaleqBuildException;
import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.TemplateValue;

public final class TemplateValueFactoryImpl implements TemplateValueFactory {

    public static interface ToTemplate {
        Set<DataType> mapsTypes();

        TemplateValue map(String fieldName, String variable);
    }

    private static final class DelegatingToTemplate implements ToTemplate {

        private final Set<DataType> mapsTypes;
        private final TemplateValue map;

        DelegatingToTemplate(final Set<DataType> mapsTypes, final TemplateValue map) {
            this.mapsTypes = mapsTypes;
            this.map = map;
        }

        @Override
        public Set<DataType> mapsTypes() {
            return mapsTypes;
        }

        @Override
        public TemplateValue map(final String fieldName, final String variable) {
            return map;
        }
    }

    private static final ToTemplate STRING_TO_TEMPLATE = new ToTemplate() {

        @Override
        public Set<DataType> mapsTypes() {
            return of(DataType.VARCHAR, DataType.LONGVARCHAR, DataType.NVARCHAR, DataType.LONGNVARCHAR, DataType.CLOB);
        }

        @Override
        public TemplateValue map(final String fieldName, final String variable) {
            return new StringTemplateValue(fieldName + "-" + variable);
        }
    };

    private static final ToTemplate NUMBER_TO_TEMPLATE = new ToTemplate() {
        @Override
        public Set<DataType> mapsTypes() {
            return of(DataType.INTEGER, DataType.SMALLINT,
                    DataType.BIGINT, DataType.REAL, DataType.DOUBLE, DataType.FLOAT, DataType.BIGINT_AUX_LONG);
        }

        @Override
        public TemplateValue map(final String fieldName, final String variable) {
            return new StringTemplateValue(variable);
        }
    };

    private static final Collection<ToTemplate> TO_TEMPLATES = ImmutableList.of(
            NUMBER_TO_TEMPLATE,
            new DelegatingToTemplate(of(DataType.NUMERIC), new ModuloTemplateValue(10000000000L)),
            new DelegatingToTemplate(of(DataType.DECIMAL), new ModuloTemplateValue(10000000000L)),
            new DelegatingToTemplate(of(DataType.TINYINT), new ModuloTemplateValue(128)),
            new DelegatingToTemplate(of(DataType.SMALLINT), new ModuloTemplateValue(32768)),
            STRING_TO_TEMPLATE,
            new DelegatingToTemplate(of(DataType.DATE), new DateTemplateValue()),
            new DelegatingToTemplate(of(DataType.TIME, DataType.TIMESTAMP),
                    new TimestampTemplateValue(Integer.MAX_VALUE / 2)),
            new DelegatingToTemplate(of(DataType.BOOLEAN, DataType.BIT), new ModuloTemplateValue(2)),
            new DelegatingToTemplate(of(DataType.CHAR, DataType.NCHAR), new CharTemplateValue()),
            new DelegatingToTemplate(
                    of(DataType.VARBINARY, DataType.BINARY, DataType.LONGVARBINARY, DataType.BLOB),
                    new Base64TemplateValue())
    );

    private final Map<DataType, ToTemplate> mapping;

    private TemplateValueFactoryImpl(final Map<DataType, ToTemplate> mapping) {
        this.mapping = mapping;
    }

    @Override
    public TemplateValue create(final DataType dataType, final String fieldName) {
        final ToTemplate toTemplate = mapping.get(dataType);
        if (toTemplate == null) {
            final String msg = String.format(
                    "Cannot create a default TemplateValue for field %s, because the DataType %s is unknown.",
                    fieldName, dataType
            );
            throw new DaleqBuildException(msg);
        }
        return toTemplate.map(fieldName, StringTemplateValue.VAR);
    }

    public static TemplateValueFactory getInstance() {
        final Map<DataType, ToTemplate> mapping = Maps.newHashMap();

        for (ToTemplate toTemplate : TO_TEMPLATES) {
            for (DataType dataType : toTemplate.mapsTypes()) {
                mapping.put(dataType, toTemplate);
            }
        }
        return new TemplateValueFactoryImpl(mapping);
    }
}
