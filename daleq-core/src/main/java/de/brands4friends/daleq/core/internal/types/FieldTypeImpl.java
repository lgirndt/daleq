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

import static com.google.common.base.Objects.toStringHelper;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldType;
import de.brands4friends.daleq.core.FieldTypeReference;
import de.brands4friends.daleq.core.FkConstraint;
import de.brands4friends.daleq.core.TemplateValue;

final class FieldTypeImpl implements FieldType {
    private final String name;
    private final DataType dataType;
    private final Optional<TemplateValue> templateValue;
    private final FieldTypeReference origin;
    private final Optional<FkConstraint> constraint;

    FieldTypeImpl(
            final String name,
            final DataType dataType,
            final Optional<TemplateValue> templateValue,
            final FieldTypeReference origin,
            final Optional<FkConstraint> constraint) {
        this.constraint = constraint;
        this.name = Preconditions.checkNotNull(name);
        this.dataType = Preconditions.checkNotNull(dataType);
        this.templateValue = Preconditions.checkNotNull(templateValue);
        this.origin = Preconditions.checkNotNull(origin);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public Optional<TemplateValue> getTemplateValue() {
        return templateValue;
    }

    @Override
    public FieldTypeReference getOrigin() {
        return origin;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof FieldTypeImpl) {
            final FieldTypeImpl that = (FieldTypeImpl) obj;

            return Objects.equal(name, that.name)
                    && Objects.equal(dataType, that.dataType)
                    && Objects.equal(templateValue, that.templateValue);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, dataType, templateValue);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("name", name)
                .add("dataType", dataType)
                .add("templateValue", templateValue)
                .toString();
    }
}
