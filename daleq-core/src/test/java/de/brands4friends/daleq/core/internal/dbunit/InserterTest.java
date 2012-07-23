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

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.easymock.EasyMockSupport;
import org.junit.Before;

import de.brands4friends.daleq.core.Daleq;
import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.TableDef;

public class InserterTest extends EasyMockSupport {

    private IDataSetFactory dataSetFactory;
    private DatabaseOperation insertOperation;
    private Inserter inserter;
    private IDatabaseConnection connection;

    @TableDef("FOO")
    public static class MyTable {
        public static final FieldDef ID = Daleq.fd(DataType.INTEGER);
        public static final FieldDef VALUE = Daleq.fd(DataType.VARCHAR);
    }

    @Before
    public void setUp() {
        dataSetFactory = createMock(IDataSetFactory.class);
        insertOperation = createMock(DatabaseOperation.class);
        inserter = new Inserter(dataSetFactory, insertOperation);
        connection = createMock(IDatabaseConnection.class);
    }

//    @Test
//    public void inserIntoDatabase_should_insertAnIDataSetWithDbUnit() throws SQLException, DatabaseUnitException {
//        final Capture<IDataSet> capturedDataset = new Capture<IDataSet>();
//        insertOperation.execute(eq(connection), capture(capturedDataset));
//
//        replayAll();
//        inserter.insertIntoDatabase(
//                Collections.singletonList(aTable(MyTable.class).with(
//                        aRow(0).f(MyTable.VALUE, "val0"),
//                        aRow(1).f(MyTable.VALUE, "val1")
//                )),connection
//
//        );
//        verifyAll();
//
//        final IDataSet dataSet = capturedDataset.getValue();
//        assertThat(dataSet.getTableNames(), arrayContaining("FOO"));
//        final ITable table = dataSet.getTable("FOO");
//        assertThat(table.getValue(0, "ID"), Matchers.is((Object) "0"));
//        assertThat(table.getValue(0, "VALUE"), Matchers.is((Object) "val0"));
//        assertThat(table.getValue(1, "ID"), Matchers.is((Object) "1"));
//        assertThat(table.getValue(1, "VALUE"), Matchers.is((Object) "val1"));
//    }
}
