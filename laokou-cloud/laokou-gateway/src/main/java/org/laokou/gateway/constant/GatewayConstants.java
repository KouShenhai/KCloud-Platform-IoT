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

package org.laokou.gateway.constant;

/**
 * @author laokou
 */
public final class GatewayConstants {

	private GatewayConstants() {
	}

	/**
	 * IP已列入黑名单.
	 */
	public static final String IP_BLACKED = "S_Gateway_IpBlacked";

	/**
	 * IP被限制.
	 */
	public static final String IP_RESTRICTED = "S_Gateway_IpRestricted";

	/**
	 * 路由不存在.
	 */
	public static final String ROUTER_NOT_EXIST = "S_Gateway_RouterNotExist";

}
