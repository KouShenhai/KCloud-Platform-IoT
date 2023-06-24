/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;

/**
 * @author laokou
 */
public class FlywaySync {

    private static final String URL = "jdbc:mysql://192.168.1.100:3306/%s?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "laokou123";

    public static void main(String[] args) {
        // base
        Flyway flyway = Flyway.configure()
                .dataSource(String.format(URL,""), USERNAME, PASSWORD)
                .locations(new Location("test"))
                .load();
        flyway.migrate();
        // sharding1


        // sharding2


        // tenant

        // nacos

        // seata
    }

}
