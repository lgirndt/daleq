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

package de.brands4friends.daleq.integration.tables;

import static de.brands4friends.daleq.core.Daleq.fd;

import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.TableDef;

@TableDef("ALL_TYPES")
public class MysqlAllTypesTable {
    // Characters
    public static final FieldDef A_VARCHAR = fd(DataType.VARCHAR);
    public static final FieldDef A_CHAR = fd(DataType.CHAR);
    //    public static final FieldDef A_LONGVARCHAR = fd(DataType.LONGVARCHAR);
    public static final FieldDef A_NVARCHAR = fd(DataType.NVARCHAR);
    public static final FieldDef A_TEXT = fd(DataType.CLOB);

    // Numerics
    public static final FieldDef A_NUMERIC = fd(DataType.NUMERIC);
    public static final FieldDef A_DECIMAL = fd(DataType.DECIMAL);
    public static final FieldDef A_BOOLEAN = fd(DataType.BOOLEAN);
    public static final FieldDef A_BIT = fd(DataType.BIT);
    public static final FieldDef A_INTEGER = fd(DataType.INTEGER);
    public static final FieldDef A_TINYINT = fd(DataType.TINYINT);
    public static final FieldDef A_SMALLINT = fd(DataType.SMALLINT);
    public static final FieldDef A_BIGINT = fd(DataType.BIGINT);
    public static final FieldDef A_REAL = fd(DataType.REAL);
    public static final FieldDef A_DOUBLE = fd(DataType.DOUBLE);
    public static final FieldDef A_FLOAT = fd(DataType.FLOAT);

    // Dates
    public static final FieldDef A_DATE = fd(DataType.DATE);
    //    public static final FieldDef A_TIME = fd(DataType.TIME);
    public static final FieldDef A_TIMESTAMP = fd(DataType.TIMESTAMP);

    // Binaries
    public static final FieldDef A_VARBINARY = fd(DataType.VARBINARY);
    //    public static final FieldDef A_BINARY = fd(DataType.BINARY);
//    public static final FieldDef A_LONGVARBINARY = fd(DataType.LONGVARBINARY);
    public static final FieldDef A_BLOB = fd(DataType.BLOB);

}
