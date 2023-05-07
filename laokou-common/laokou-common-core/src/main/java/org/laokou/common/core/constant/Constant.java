/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.core.constant;
/**
 * 常量
 * @author laokou
 */
public interface Constant {

    String FORWARD_SLASH = "/";

    String DOT = ".";
    String RISK = ":";

    String COMMA = ",";

    String AUTHORIZATION_HEAD = "Authorization";
    String USER_NAME = "user-name";
    String TRACE_ID = "trace-id";
    String USER_ID = "user-id";
    String TENANT_ID = "tenant-id";

    int NO = 0;

    int YES = 1;

    String UNDERLINE = "_";

    String AND = "&";

    int DEFAULT = 0;

    String PLACE_HOLDER = "$$";

    String SHARDING_SPHERE = "shardingSphere";

    String SHARDING_SPHERE_READWRITE = "shardingSphereReadWrite";

    String TENANT = "#tenant";

    /**
     * 默认数据库
     */
    String DEFAULT_SOURCE = "master";

    String ACCEPT_LANGUAGE = "Accept-Language";

}
