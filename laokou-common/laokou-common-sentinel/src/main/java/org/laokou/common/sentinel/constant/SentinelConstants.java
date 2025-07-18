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

package org.laokou.common.sentinel.constant;

/**
 * @author laokou
 */
public final class SentinelConstants {

	private SentinelConstants() {
	}

	/**
	 * 授权规则错误.
	 */
	public static final String AUTHORITY = "S_Sentinel_Authority";

	/**
	 * 系统规则错误.
	 */
	public static final String SYSTEM_BLOCKED = "S_Sentinel_SystemBlocked";

	/**
	 * 热点参数已限流.
	 */
	public static final String PARAM_FLOWED = "S_Sentinel_ParamFlowed";

	/**
	 * 已降级.
	 */
	public static final String DEGRADED = "S_Sentinel_Degraded";

	/**
	 * 已限流.
	 */
	public static final String FLOWED = "S_Sentinel_Flowed";

}
