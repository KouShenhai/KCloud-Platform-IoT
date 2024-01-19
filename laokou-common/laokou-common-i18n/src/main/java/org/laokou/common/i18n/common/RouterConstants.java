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
@Schema(name = "RouterConstants", description = "路由常量")
public final class RouterConstants {

	private RouterConstants() {
	}

	@Schema(name = "SERVICE_HOST", description = "服务IP")
	public static final String SERVICE_HOST = "service-host";

	@Schema(name = "SERVICE_PORT", description = "服务端口")
	public static final String SERVICE_PORT = "service-port";

	@Schema(name = "GENERATED_NAME_PREFIX", description = "生成名称前缀")
	public static final String GENERATED_NAME_PREFIX = "_genkey_";

	@Schema(name = "DATA_ID", description = "Nacos配置标识")
	public static final String DATA_ID = "router.json";

	@Schema(name = "API_URL_PREFIX", description = "Api路径前缀")
	public static final String API_URL_PREFIX = "/v1/routers";

}
