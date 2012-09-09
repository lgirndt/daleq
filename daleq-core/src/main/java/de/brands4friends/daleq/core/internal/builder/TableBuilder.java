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
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

import de.brands4friends.daleq.core.Context;
import de.brands4friends.daleq.core.Daleq;
import de.brands4friends.daleq.core.FieldTypeReference;
import de.brands4friends.daleq.core.Row;
import de.brands4friends.daleq.core.RowData;
import de.brands4friends.daleq.core.Table;
import de.brands4friends.daleq.core.TableData;
import de.brands4friends.daleq.core.TableType;
import de.brands4friends.daleq.core.TableTypeReference;
import de.brands4friends.daleq.core.internal.types.TableTypeRepository;

public final class TableBuilder implements Table {

    private final TableTypeReference tableRef;
    private final List<Row> rows;

    private TableBuilder(final TableTypeReference tableRef) {
        this.tableRef = Preconditions.checkNotNull(tableRef);
        this.rows = Lists.newArrayList();
    }

    @Override
    public Table with(final Row... rows) {
        this.rows.addAll(Arrays.asList(rows));
        return this;
    }

    @Override
    public Table withSomeRows(final Iterable<Long> ids) {
        for (long id : ids) {
            this.rows.add(Daleq.aRow(id));
        }
        return this;
    }

    @Override
    public Table withSomeRows(final long... ids) {
        return withSomeRows(Longs.asList(ids));
    }

    @Override
    public Table withRowsUntil(final long maxId) {
        // TODO check parameter!
        for (long i = 0; i < maxId; i++) {
            this.rows.add(Daleq.aRow(i));
        }
        return this;
    }

    @Override
    public Table withRowsBetween(final long from, final long to) {
        Preconditions.checkArgument(from < to,
                "Parameter from should be less than parameter to, but %s<%s is not true.", from, to);
        for (long i = from; i <= to; i++) {
            this.rows.add(Daleq.aRow(i));
        }
        return this;
    }

    @Override
    public Table allHaving(final FieldTypeReference fieldDef, @Nullable final Object value) {
        for (Row row : rows) {
            row.f(fieldDef, value);
        }
        return this;
    }

    @Override
    public Table having(final FieldTypeReference fieldDef, final Object... values) {
        return havingIterable(fieldDef, Arrays.asList(values));
    }

    @Override
    public Table havingIterable(final FieldTypeReference fieldDef, final Iterable<Object> values) {
        Preconditions.checkNotNull(values);
        final Iterator<Object> iter = values.iterator();
        for (Row row : rows) {
            if (!iter.hasNext()) {
                return this;
            }
            row.f(fieldDef, iter.next());
        }
        return this;
    }

    @Override
    public TableData build(final Context context) {
        final TableType tableType = toTableType(context);
        final List<RowData> rows = Lists.transform(this.rows, new Function<Row, RowData>() {
            @Override
            public RowData apply(final Row row) {
                return row.build(context, tableType);
            }
        });
        return new ImmutableTableData(tableType, rows);
    }

    private TableType toTableType(final Context context) {
        final TableTypeRepository repository = context.getService(TableTypeRepository.class);
        return repository.get(tableRef);
    }

    public static Table aTable(final TableTypeReference tableReference) {
        return new TableBuilder(tableReference);
    }
}
