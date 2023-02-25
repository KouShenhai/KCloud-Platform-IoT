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
 * gateway  => 1
 * auth     => 2
 * admin    => 3
 * tenant   => 4
 * sentinel => 5
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
     * 未知错误
     */
    int SERVICE_UNKNOWN_ERROR = 1002;

    /**
     * gateway => 1
     * IP被列入黑名单，请联系管理员
     */
    int IP_BLACK = 1003;

    /**
     * auth => 2
     * 唯一标识不能为空
     */
    int IDENTIFIER_NOT_NULL = 2001;

    /**
     * auth => 2
     * 验证码不能为空
     */
    int CAPTCHA_NOT_NULL = 2002;

    /**
     * auth => 2
     * 验证码不正确，请重新输入
     */
    int CAPTCHA_ERROR = 2003;

    /**
     * auth => 2
     * 验证码已过期
     */
    int CAPTCHA_EXPIRED = 2004;

    /**
     * auth => 2
     * 账号不能为空
     */
    int USERNAME_NOT_NULL = 2005;

    /**
     * auth => 2
     * 账号已被停用
     */
    int USERNAME_DISABLE = 2006;

    /**
     * auth => 2
     * 帐户或密码错误，请重新输入
     */
    int USERNAME_PASSWORD_ERROR = 2007;

    /**
     * auth => 2
     * 用户没有权限访问，请联系管理员
     */
    int USERNAME_NOT_PERMISSION = 2008;

    /**
     * auth => 2
     * 密码不能为空
     */
    int PASSWORD_NOT_NULL = 2009;

    /**
     * auth => 2
     * 手机号不能为空
     */
    int MOBILE_NOT_NULL = 2010;

    /**
     * auth => 2
     * 手机号错误，请重新输入
     */
    int MOBILE_ERROR = 2011;

    /**
     * auth => 2
     * 邮箱不能为空
     */
    int MAIL_NOT_NULL = 2012;

    /**
     * auth => 2
     * 邮箱错误，请重新输入
     */
    int MAIL_ERROR = 2013;

    /**
     * auth => 2
     * 租户不能为空，请选择租户
     */
    int TENANT_NOT_NULL = 2014;

    /**
     * auth => 2
     * 无效作用域
     */
    int INVALID_SCOPE = 2015;

    /**
     * sentinel => 5
     * 接口已被限流，请稍后再试
     */
    int API_BLOCK_REQUEST = 5001;

}
