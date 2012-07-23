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

import java.sql.SQLException;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import de.brands4friends.daleq.core.TableData;

public class Inserter {

    private final IDataSetFactory dataSetFactory;
    private final DatabaseOperation insertOperation;

    public Inserter(final IDataSetFactory dataSetFactory, final DatabaseOperation insertOperation) {
        this.dataSetFactory = dataSetFactory;
        this.insertOperation = insertOperation;
    }

    public void insertIntoDatabase(final List<TableData> tables, final IDatabaseConnection databaseConnection)
            throws DatabaseUnitException, SQLException {
        final IDataSet dbUnitDataset = dataSetFactory.create(tables);
        insertOperation.execute(databaseConnection, dbUnitDataset);
    }

}
