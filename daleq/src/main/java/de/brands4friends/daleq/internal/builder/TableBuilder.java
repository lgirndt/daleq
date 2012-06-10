package de.brands4friends.daleq.internal.builder;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

import de.brands4friends.daleq.Context;
import de.brands4friends.daleq.Daleq;
import de.brands4friends.daleq.Row;
import de.brands4friends.daleq.RowContainer;
import de.brands4friends.daleq.Table;
import de.brands4friends.daleq.TableContainer;
import de.brands4friends.daleq.internal.types.TableType;
import de.brands4friends.daleq.internal.types.TableTypeFactory;

public class TableBuilder implements Table {

    private final TableType tableType;
    private final List<Row> rows;

    public TableBuilder(final TableType tableType) {
        this.tableType = tableType;
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
        for (long i = 0; i < maxId; i++) {
            this.rows.add(Daleq.aRow(i));
        }
        return this;
    }

    @Override
    public TableContainer build(final Context context) {
        final List<RowContainer> rowContainers = Lists.transform(rows, new Function<Row, RowContainer>() {
            @Override
            public RowContainer apply(final Row row) {
                return row.build(context, tableType);
            }
        });
        return new TableContainerImpl(tableType.getName(), rowContainers);
    }

    public static <T> TableBuilder aTable(final Class<T> fromClass) {
        final TableType tableType = new TableTypeFactory().create(fromClass);
        return new TableBuilder(tableType);
    }
}
