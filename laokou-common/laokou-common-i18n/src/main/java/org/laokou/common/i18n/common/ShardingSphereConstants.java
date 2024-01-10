/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.i18n.common;

import java.util.regex.Pattern;

/**
 * @author laokou
 */
public final class ShardingSphereConstants {

    private ShardingSphereConstants() {}

    public static final String JDBC_TYPE = "jdbc:shardingsphere:";

    public static final String NACOS_TYPE = "nacos:";

    public static final String YAML_LOCATION = "bootstrap.yml";

    public static final String YAML_FORMAT = "yaml";

    public static final Pattern ENC_PATTERN = Pattern.compile("^ENC\\((.*)\\)$");

}
