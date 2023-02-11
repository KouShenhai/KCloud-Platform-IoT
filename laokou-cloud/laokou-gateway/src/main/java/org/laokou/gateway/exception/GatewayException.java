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
package org.laokou.gateway.exception;

import lombok.Getter;

/**
 * @author laokou
 */
public enum GatewayException {

    /**
     * 服务正在维护，请联系管理员
     */
    SERVICE_MAINTENANCE(500,"服务正在维护，请联系管理员"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401,"未授权"),
    /**
     * 未知错误
     */
    UNKNOWN(505,"未知错误"),
    /**
     * 操作太频繁，请稍后再试
     */
    BLOCK_REQUEST(429,"操作太频繁，请稍后再试");

    @Getter
    private final int code;
    @Getter
    private final String msg;

    GatewayException(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

}
