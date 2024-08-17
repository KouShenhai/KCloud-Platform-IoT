/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common.constant;

/**
 * 链路常量.
 *
 * @author laokou
 */
public final class TraceConstant {

	/**
	 * 用户名.
	 */
	public static final String USER_NAME = "user-name";

	/**
	 * 链路ID.
	 */
	public static final String TRACE_ID = "trace-id";

	/**
	 * 用户ID.
	 */
	public static final String USER_ID = "user-id";

	/**
	 * 租户ID.
	 */
	public static final String TENANT_ID = "tenant-id";

	/**
	 * 域名.
	 */
	public static final String DOMAIN_NAME = "domain-name";

	/**
	 * 请求ID.
	 */
	public static final String REQUEST_ID = "request-id";
	
	public static final String SPAN_USER_ID = "user.id";
	public static final String SPAN_USER_NAME = "user.name";
	public static final String SPAN_TRACE_ID = "trace.id";
	public static final String SPAN_TENANT_ID = "tenant.id";

	private TraceConstant() {
	}

}
