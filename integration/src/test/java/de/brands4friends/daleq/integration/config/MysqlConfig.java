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

package de.brands4friends.daleq.integration.config;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.sql.DataSource;

import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.brands4friends.daleq.integration.beans.PrepareMysqlSchema;
import de.brands4friends.daleq.integration.beans.TableProvider;
import de.brands4friends.daleq.integration.tables.MysqlAllTypesTable;

@Configuration
@Profile("Mysql")
public class MysqlConfig implements DbConfig {

    public static final String MYSQL_URL = "integration.mysql.url";
    public static final String MYSQL_USER = "integration.mysql.user";
    public static final String MYSQL_PASSWORD = "integration.mysql.password";

    @Bean
    public DataSource dataSource() {

        final String url = readProperty(MYSQL_URL);
        final String user = readProperty(MYSQL_USER);
        final String password = readProperty(MYSQL_PASSWORD);

        return new DriverManagerDataSource(url, user, password);
    }

    private String readProperty(final String property) {
        return checkNotNull(System.getProperty(property), "Expected System Property '%s'", property);
    }

    @Bean
    public IDataTypeFactory dataTypeFactory() {
        return new MySqlDataTypeFactory();
    }

    @Bean
    public TableProvider allTypesProvider() {
        return new TableProvider(MysqlAllTypesTable.class);
    }

    @Bean
    public PrepareMysqlSchema prepareMysqlSchema() {
        return new PrepareMysqlSchema(dataSource());
    }
}
