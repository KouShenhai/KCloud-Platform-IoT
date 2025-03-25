/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
public final class TraceConstants {

	/**
	 * 链路ID.
	 */
	public static final String TRACE_ID = "trace-id";

	/**
	 * 请求ID.
	 */
	public static final String REQUEST_ID = "request-id";

	/**
	 * 标签ID.
	 */
	public static final String SPAN_ID = "span-id";

	/**
	 * 服务主机.
	 */
	public static final String SERVICE_HOST = "service-host";

	/**
	 * 服务端口.
	 */
	public static final String SERVICE_PORT = "service-port";

	/**
	 * 服务灰度.
	 */
	public static final String SERVICE_GRAY = "service-gray";

	private TraceConstants() {
	}

}
