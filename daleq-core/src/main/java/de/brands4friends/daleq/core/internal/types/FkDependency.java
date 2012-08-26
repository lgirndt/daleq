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
import com.google.common.base.Preconditions;

import de.brands4friends.daleq.core.TableType;

public final class FkDependency {
    private final TableType from;
    private final TableType to;

    public FkDependency(final TableType from, final TableType to) {
        this.from = Preconditions.checkNotNull(from);
        this.to = Preconditions.checkNotNull(to);
    }

    public TableType getFrom() {
        return from;
    }

    public TableType getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FkDependency) {
            final FkDependency that = (FkDependency) obj;

            return Objects.equal(from, that.from)
                    && Objects.equal(to, that.to);
        }

        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("from", from)
                .add("to", to)
                .toString();
    }
}
