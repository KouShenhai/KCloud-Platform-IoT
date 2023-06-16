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

    /**
     * 分割
     */
    String FORWARD_SLASH = "/";

    /**
     * 分割
     */
    String DOT = ".";

    /**
     * 分割
     */
    String RISK = ":";

    /**
     * 分割
     */
    String COMMA = ",";

    /**
     * 认证
     */
    String AUTHORIZATION_HEAD = "Authorization";

    /**
     * 用户名
     */
    String USER_NAME = "user-name";

    /**
     * 链路ID
     */
    String TRACE_ID = "trace-id";

    /**
     * 用户ID
     */
    String USER_ID = "user-id";

    /**
     * 租户
     */
    String TENANT_ID = "tenant-id";

    /**
     * 签名
     */
    String SIGN = "sign";

    /**
     * 时间戳
     */
    String TIMESTAMP = "timestamp";

    /**
     * 使用状态
     */
    int USE_STATUS = 1;

    /**
     * 默认
     */
    int DEFAULT = 0;

    /**
     * 并
     */
    String AND = "&";

    /**
     * 表切换
     */
    String PLACE_HOLDER = "$$";

    /**
     * 分库分表
     */
    String SHARDING_SPHERE = "shardingSphere";

    /**
     * 读写分离
     */
    String SHARDING_SPHERE_READWRITE = "shardingSphereReadWrite";

    /**
     * 多租户
     */
    String TENANT = "#tenant";

    /**
     * 请求参数
     */
    String QUESTION_MARK = "?";

    /**
     * 默认数据库
     */
    String DEFAULT_SOURCE = "master";

    /**
     * 国际化
     */
    String ACCEPT_LANGUAGE = "Accept-Language";

    /**
     * 接口幂等性
     */
    String REQUEST_ID = "request-id";

    String TRUE = "true";

    String REGISTER_TRUST_CERT_PATH = "register.cer";

}
