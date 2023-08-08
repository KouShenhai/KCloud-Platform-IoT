/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

/**
 * 状态码
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
	 * 请求太多，已被限流，请稍后再试
	 */
	int TOO_MANY_REQUESTS = 429;

	/**
	 * 服务器内部错误，无法完成请求
	 */
	int INTERNAL_SERVER_ERROR = 500;

	/**
	 * 服务正在维护，请联系管理员
	 */
	int SERVICE_UNAVAILABLE = 503;

}
