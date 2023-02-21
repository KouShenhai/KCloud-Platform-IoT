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
package org.laokou.common.i18n.core;

/**
 * 状态码
 * 不包括 200、400、401、403、404、500
 * 编码由5位数字构成
 * 前2位为应用编号
 * 后3位为业务编号
 * gateway => 1
 * auth    => 2
 * admin   => 3
 * @author laokou
 */
public interface StatusCode {

    /**
     * 请求成功
     */
    int OK = 200;

    /**
     * 错误请求
     */
    int BAD_REQUEST = 400;

    /**
     * 登录状态已过期，请重新登录
     */
    int UNAUTHORIZED = 401;

    /**
     * 访问拒绝，没有权限
     */
    int FORBIDDEN = 403;

    /**
     * 无法找到请求的资源
     */
    int NOT_FOUND = 404;

    /**
     * 服务器内部错误，无法完成请求
     */
    int INTERNAL_SERVER_ERROR= 500;

    /**
     * 服务正在维护，请联系管理员
     */
    int SERVICE_UNAVAILABLE = 503;

    /**
     * gateway => 1
     * 请求过于频繁，请稍后再试
     */
    int SERVICE_BLOCK_REQUEST = 1001;

    /**
     * gateway => 1
     * 服务未知错误
     */
    int SERVICE_UNKNOWN_ERROR = 1002;

}
