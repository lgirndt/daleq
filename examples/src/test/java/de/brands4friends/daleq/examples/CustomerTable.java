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

package de.brands4friends.daleq.examples;

import static de.brands4friends.daleq.core.Daleq.fd;
import static de.brands4friends.daleq.core.DataType.VARCHAR;

import de.brands4friends.daleq.core.DataType;
import de.brands4friends.daleq.core.FieldDef;
import de.brands4friends.daleq.core.TableDef;

@TableDef("CUSTOMER")
public class CustomerTable {
    public static final FieldDef ID = fd(DataType.INTEGER);
    public static final FieldDef FAMILY_NAME = fd(VARCHAR);
    public static final FieldDef GIVEN_NAME = fd(VARCHAR);
    public static final FieldDef EMAIL = fd(VARCHAR);
}
