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

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.FieldType;
import de.brands4friends.daleq.core.FkConstraint;
import de.brands4friends.daleq.core.internal.builder.FieldDefBuilder;

/**
 * Scans classes for FieldDefs and returns the findings as FieldTypes
 */
class FieldScanner {

    private static final Function<Field, FieldType> MAP_FIELD_TO_FIELD_TYPE = new Function<Field, FieldType>() {
        @Override
        public FieldType apply(@Nullable final Field field) {
            try {
                if (field == null) {
                    return null;
                }

                final FieldDef fieldDef = (FieldDef) field.get(null);
                final String name = fieldDef.getName().or(field.getName());
                final DataType dataType = fieldDef.getDataType();
                final Optional<FkConstraint> constraint = Optional.absent();
                return new FieldTypeImpl(name, dataType, fieldDef.getTemplate(), fieldDef, constraint);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    };

    private static final Predicate<Field> IS_FIELD_DEF = new Predicate<Field>() {
        @Override
        public boolean apply(@Nullable final Field field) {
            if (field == null) {
                return false;
            }
            return field.getType().isAssignableFrom(FieldDefBuilder.class);
        }
    };

    private static final Predicate<Field> IS_CONSTANT = new Predicate<Field>() {
        @Override
        public boolean apply(@Nullable final Field field) {
            if (field == null) {
                return false;
            }
            final int modifiers = field.getModifiers();
            return Modifier.isStatic(modifiers)
                    && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers);
        }
    };

    public <T> List<FieldType> scan(final Class<T> fromClass) {
        final List<FieldType> result = Lists.newArrayList(
                transform(
                        filter(toDeclaredFields(fromClass), and(IS_FIELD_DEF, IS_CONSTANT)),
                        MAP_FIELD_TO_FIELD_TYPE)
        );
        checkResultHasFields(fromClass, result);
        return result;
    }

    private <T> List<Field> toDeclaredFields(final Class<T> fromClass) {
        return Arrays.asList(fromClass.getDeclaredFields());
    }

    private <T> void checkResultHasFields(final Class<T> fromClass, final List<FieldType> result) {
        if (result.isEmpty()) {
            throw new IllegalArgumentException(
                    "No Field Definitions in class '" + fromClass.getSimpleName() + "'");
        }
    }

}
