/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "RequestHeaderConstants", description = "请求头常量")
public final class RequestHeaderConstants {

	private RequestHeaderConstants() {
	}

	@Schema(name = "AUTHORIZATION", description = "认证标识")
	public static final String AUTHORIZATION = "Authorization";

	@Schema(name = "CHUNKED", description = "Chunked")
	public static final String CHUNKED = "chunked";

	@Schema(name = "UPGRADE", description = "Upgrade")
	public static final String UPGRADE = "Upgrade";

	@Schema(name = "WEBSOCKET", description = "WebSocket")
	public static final String WEBSOCKET = "websocket";

	@Schema(name = "NONCE", description = "随机字符")
	public static final String NONCE = "nonce";

	@Schema(name = "SIGN", description = "签名（MD5）")
	public static final String SIGN = "sign";

	@Schema(name = "TIMESTAMP", description = "时间戳")
	public static final String TIMESTAMP = "timestamp";

	@Schema(name = "APP_KEY", description = "应用标识")
	public static final String APP_KEY = "app-key";

	@Schema(name = "APP_SECRET", description = "应用密钥")
	public static final String APP_SECRET = "app-secret";

}
