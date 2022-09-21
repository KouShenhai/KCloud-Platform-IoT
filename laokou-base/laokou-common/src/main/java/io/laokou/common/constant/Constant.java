/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package io.laokou.common.constant;
/**
 * 常量
 * @author Kou Shenhai
 */
public interface Constant {

    String ACCESS_TOKEN = "access_token";

    String PASSWORD_HEAD = "password";

    String REDIRECT_URL_HEAD = "redirectUrl";

    String URI = "uri";

    String METHOD = "method";

    String UUID = "uuid";
    /**
     * Authorization header
     */
    String AUTHORIZATION_HEAD = "Authorization";
    /**
     * 成功
     */
    Integer SUCCESS = 1;
    /**
     * 失败
     */
    Integer FAIL = 0;
    /**
     * header
     */
    String HEADER = "header";
    /**
     * 用户标识
     */
    String USER_KEY_HEAD = "userId";
    /**
     * 用户名
     */
    String USERNAME_HEAD = "username";
    /**
     * no
     */
    Integer NO = 0;
    /**
     * yes
     */
    Integer YES = 1;
    /**
     * 认证标识
     */
    String TICKET = "ticket";

    String COMMA = ",";

    String UNKNOWN = "unknown";
}
