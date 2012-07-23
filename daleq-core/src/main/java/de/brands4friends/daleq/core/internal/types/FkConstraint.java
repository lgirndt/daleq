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

import com.google.common.base.Objects;

import de.brands4friends.daleq.core.FieldTypeReference;
import de.brands4friends.daleq.core.TableTypeReference;

public final class FkConstraint {
    private final TableTypeReference tableRef;
    private final FieldTypeReference fieldRef;

    public FkConstraint(final TableTypeReference tableRef, final FieldTypeReference fieldRef) {
        this.tableRef = tableRef;
        this.fieldRef = fieldRef;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(tableRef, fieldRef);
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof FkConstraint) {
            final FkConstraint that = (FkConstraint) obj;

            return Objects.equal(tableRef, that.tableRef)
                    && Objects.equal(fieldRef, that.fieldRef);
        }

        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("tableRef", tableRef).add("fieldRef", fieldRef).toString();
    }
}
