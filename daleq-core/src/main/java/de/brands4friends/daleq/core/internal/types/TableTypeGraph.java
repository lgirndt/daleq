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

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.brands4friends.daleq.core.TableType;

public class TableTypeGraph {

    private final Set<TableType> vertices;
    private final Set<FkDependency> edges;

    public TableTypeGraph(final Set<FkDependency> edges) {
        this.edges = ImmutableSet.copyOf(edges);
        this.vertices = Sets.newHashSet();
        for (FkDependency edge : edges) {
            vertices.add(edge.getFrom());
            vertices.add(edge.getTo());
        }
    }
}
