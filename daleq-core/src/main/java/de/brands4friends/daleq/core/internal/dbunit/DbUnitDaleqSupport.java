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

package de.brands4friends.daleq.core.internal.dbunit;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.brands4friends.daleq.core.Context;
import de.brands4friends.daleq.core.DaleqException;
import de.brands4friends.daleq.core.DaleqSupport;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.Table;
import de.brands4friends.daleq.core.TableData;
import de.brands4friends.daleq.core.internal.builder.SimpleContext;
import de.brands4friends.daleq.core.internal.dbunit.dataset.InMemoryDataSetFactory;
import de.brands4friends.daleq.core.internal.formatting.MarkdownTableFormatter;

public class DbUnitDaleqSupport implements DaleqSupport {

    private final ConnectionFactory connectionFactory;
    private final Inserter inserter;

    private final Context context;
    private final Asserter asserter;

    DbUnitDaleqSupport(
            final ConnectionFactory connectionFactory,
            final Asserter asserter,
            final Inserter inserter) {

        this.connectionFactory = connectionFactory;
        this.inserter = inserter;
        this.asserter = asserter;

        this.context = new SimpleContext();
    }

    public static DbUnitDaleqSupport createInstance(final ConnectionFactory connectionFactory) {
        final IDataSetFactory dataSetFactory = new InMemoryDataSetFactory();
        final DatabaseOperation insertOperation = DatabaseOperation.INSERT;
        final Asserter asserter = new Asserter(dataSetFactory, connectionFactory);
        final Inserter inserter = new Inserter(dataSetFactory, insertOperation);
        return new DbUnitDaleqSupport(connectionFactory, asserter, inserter);
    }

    /**
     * Returns a DatabaseConnection which is aware of Spring's Transaction Management.
     * <p/>
     * As a matter of fact this works if and only if we are already in an active Transaction due to the way
     * Spring's Transaction Manager works. Hence we have to create a new DbUnit Database Connection each time
     * we are going to insert data in the db.
     *
     * @return a transaction aware connection to the database.
     * @throws de.brands4friends.daleq.core.DaleqException
     *          if DbUnit denies the creation of the IDatabaseConnection
     */
    private IDatabaseConnection createDatabaseConnection() {
        Preconditions.checkNotNull(connectionFactory, "connectionFactory is null.");
        return connectionFactory.createConnection();
    }

    /**
     * Inserts the given tables into the database.
     * <p/>
     * The insertion respects the current transaction context, hence if they are written in an active transaction, they
     * are properly roled back.
     */
    @Override
    public final void insertIntoDatabase(final Table... tables) {
        try {
            inserter.insertIntoDatabase(toTables(tables), createDatabaseConnection());

        } catch (DatabaseUnitException e) {
            throw new DaleqException(e);
        } catch (SQLException e) {
            throw new DaleqException(e);
        }
    }

    private List<TableData> toTables(final Table... tables) {
        return Lists.transform(
                Arrays.asList(tables),
                new Function<Table, TableData>() {
                    @Override
                    public TableData apply(final Table table) {
                        return table.build(context);
                    }
                });
    }

    @Override
    public void assertTableInDatabase(final Table table, final FieldDef... ignoreColumns) {
        Preconditions.checkNotNull(table);
        final List<TableData> allTables = toTables(table);
        assertTableInDatabase(allTables, ignoreColumns);
    }

    private void assertTableInDatabase(final List<TableData> allTables, final FieldDef[] ignoreColumns) {
        asserter.assertTableInDatabase(allTables, ignoreColumns);
    }

    @Override
    public void printTable(final Table table, final PrintStream printer) throws IOException {
        Preconditions.checkNotNull(table);
        final TableData tableData = table.build(context);
        new MarkdownTableFormatter().formatTo(tableData, printer);
    }

}
