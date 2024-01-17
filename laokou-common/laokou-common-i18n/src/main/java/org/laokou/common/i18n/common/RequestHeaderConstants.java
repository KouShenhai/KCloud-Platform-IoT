/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
 * 请求头常量.
 * @author laokou
 */
public final class RequestHeaderConstants {

	private RequestHeaderConstants() {
	}

	/**
	 * 请求头-认证标识.
	 */
	public static final String AUTHORIZATION = "Authorization";

	/**
	 * chunked.
	 */
	public static final String CHUNKED = "chunked";

	/**
	 * Upgrade.
	 */
	public static final String UPGRADE = "Upgrade";

	/**
	 * websocket.
	 */
	public static final String WEBSOCKET = "websocket";

	/**
	 * 随机字符.
	 */
	public static final String NONCE = "nonce";

	/**
	 * 签名（MD5）.
	 */
	public static final String SIGN = "sign";

	/**
	 * 时间戳.
	 */
	public static final String TIMESTAMP = "timestamp";

	/**
	 * 应用标识.
	 */
	public static final String APP_KEY = "app-key";

	/**
	 * 应用密钥.
	 */
	public static final String APP_SECRET = "app-secret";

	/**
	 * 路由标识.
	 */
	public static final String ROUTER_KEY = "router-key";

}
