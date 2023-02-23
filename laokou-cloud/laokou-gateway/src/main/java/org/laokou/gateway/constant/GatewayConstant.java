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
package org.laokou.gateway.constant;

/**
 * @author laokou
 */
public interface GatewayConstant {

    /**
     * 请求链-用户id
     */
    String REQUEST_USER_ID = "userId";
    /**
     * 请求链-用户名
     */
    String REQUEST_USERNAME = "username";
    /**
     * 密码模式-请求地址
     */
    String OAUTH2_AUTH_URI = "/**/oauth2/token";

    /**
     * 密码模式-用户名
     */
    String USERNAME = "username";

    /**
     * OAuth2错误信息
     */
    String ERROR_DESCRIPTION = "error_description";

    /**
     * OAuth2错误码
     */
    String ERROR = "error";

    /**
     * 密码模式-密码
     */
    String PASSWORD = "password";

    /**
     * xss 请求头
     */
    String XSS_TICKET_HEADER = "xss-ticket";
    /**
     * xss 白名单
     */
    String XSS_TICKET_VALUE = "xss-white";

}
