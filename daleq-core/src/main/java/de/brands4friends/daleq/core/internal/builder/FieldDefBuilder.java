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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.FieldType;
import de.brands4friends.daleq.core.FieldTypeReference;
import de.brands4friends.daleq.core.TableType;
import de.brands4friends.daleq.core.TemplateValue;
import de.brands4friends.daleq.core.internal.template.StringTemplateValue;
import de.brands4friends.daleq.core.internal.types.ClassBasedTableTypeReference;
import de.brands4friends.daleq.core.internal.types.FkConstraint;

public final class FieldDefBuilder implements FieldDef {

    private final DataType dataType;
    private final Optional<String> name;
    private final Optional<TemplateValue> template;
    private final Optional<FkConstraint> fkConstraint;

    private FieldDefBuilder(
            final DataType dataType,
            final Optional<String> name,
            final Optional<TemplateValue> template, final Optional<FkConstraint> fkConstraint) {
        this.fkConstraint = fkConstraint;
        this.dataType = Preconditions.checkNotNull(dataType);
        this.name = Preconditions.checkNotNull(name);
        this.template = Preconditions.checkNotNull(template);
    }

    @Override
    public Optional<String> getName() {
        return name;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public Optional<TemplateValue> getTemplate() {
        return template;
    }

    @Override
    public Optional<FkConstraint> getFkConstraint() {
        return fkConstraint;
    }

    @Override
    public FieldDef name(final String name) {
        Preconditions.checkNotNull(name);
        return new FieldDefBuilder(this.dataType, Optional.of(name), this.template, fkConstraint);
    }

    @Override
    public FieldDef template(final String template) {
        Preconditions.checkNotNull(template);
        return new FieldDefBuilder(
                this.dataType,
                this.name,
                Optional.<TemplateValue>of(new StringTemplateValue(template)),
                fkConstraint);
    }

    @Override
    public <T> FieldDef fkConstraint(final Class<T> tableRef, final FieldTypeReference fieldRef) {
        Preconditions.checkNotNull(tableRef);
        Preconditions.checkNotNull(fieldRef);

        return new FieldDefBuilder(
                this.dataType,
                this.name,
                this.template,
                Optional.of(new FkConstraint(ClassBasedTableTypeReference.of(tableRef), fieldRef))
        );
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("name", name).add("dataType", dataType).toString();
    }

    public static FieldDef fd(final DataType dataType) {
        Preconditions.checkNotNull(dataType);
        return new FieldDefBuilder(
                dataType,
                Optional.<String>absent(),
                Optional.<TemplateValue>absent(),
                Optional.<FkConstraint>absent()
        );
    }

    @Override
    public FieldType resolve(final TableType tableType) {
        return tableType.findFieldBy(this);
    }
}
