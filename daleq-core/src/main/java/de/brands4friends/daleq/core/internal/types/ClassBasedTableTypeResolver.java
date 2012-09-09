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

import de.brands4friends.daleq.core.TableType;
import de.brands4friends.daleq.core.TableTypeReference;

/**
 *
 */
public class ClassBasedTableTypeResolver implements TableTypeResolver {

    private final TableTypeFactory tableTypeFactory;

    public ClassBasedTableTypeResolver() {
        this.tableTypeFactory = new TableTypeFactory();
    }

    @Override
    public boolean canResolve(final TableTypeReference reference) {
        return reference instanceof ClassBasedTableTypeReference;
    }

    @Override
    public TableType resolve(final TableTypeReference reference) {
        final ClassBasedTableTypeReference<?> classBased = asClassBased(reference);
        return tableTypeFactory.create(classBased.getTable());
    }

    private ClassBasedTableTypeReference<?> asClassBased(final TableTypeReference reference) {
        if (!canResolve(reference)) {
            throw new IllegalArgumentException(
                    "The given TableTypeReference could not be resolved by the ClassBasedTableTypeResolver.");
        }
        return (ClassBasedTableTypeReference<?>) reference;
    }
}
