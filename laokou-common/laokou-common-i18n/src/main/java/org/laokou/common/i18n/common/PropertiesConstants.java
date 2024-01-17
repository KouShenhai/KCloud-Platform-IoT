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

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "PropertiesConstants", description = "Properties常量")
public final class PropertiesConstants {

	private PropertiesConstants() {
	}

	@Schema(name = "SPRING_APPLICATION_NAME", description = "应用名称")
	public static final String SPRING_APPLICATION_NAME = "spring.application.name";

	@Schema(name = "XXL_JOB_PREFIX", description = "XXL-JOB配置前缀")
	public static final String XXL_JOB_PREFIX = "spring.xxl-job";

	@Schema(name = "DEFAULT_CONFIG_PREFIX", description = "默认配置前缀")
	public static final String DEFAULT_CONFIG_PREFIX = "spring.default-config";

	@Schema(name = "OAUTH2_AUTHORIZATION_SERVER_PREFIX", description = "OAuth2认证配置前缀")
	public static final String OAUTH2_AUTHORIZATION_SERVER_PREFIX = "spring.security.oauth2.authorization-server";

	@Schema(name = "OAUTH2_RESOURCE_SERVER_PREFIX", description = "OAuth2资源配置前缀")
	public static final String OAUTH2_RESOURCE_SERVER_PREFIX = "spring.security.oauth2.resource-server";

	@Schema(name = "WEBSOCKET_PREFIX", description = "WebSocket配置前缀")
	public static final String WEBSOCKET_PREFIX = "spring.websocket";

	@Schema(name = "SLOW_SQL_PREFIX", description = "慢SQL配置前缀")
	public static final String SLOW_SQL_PREFIX = "slow-sql";

	@Schema(name = "OPENAPI_DOC_PREFIX", description = "OpenApi-Doc配置前缀")
	public static final String OPENAPI_DOC_PREFIX = "openapi-doc";

	@Schema(name = "MQTT_PREFIX", description = "MQTT配置前缀")
	public static final String MQTT_PREFIX = "spring.mqtt";

	@Schema(name = "IP_PREFIX", description = "黑/白名单配置前缀")
	public static final String IP_PREFIX = "spring.cloud.gateway.ip";

	@Schema(name = "ROUTER_AUTH_PREFIX", description = "路由认证配置前缀")
	public static final String ROUTER_AUTH_PREFIX = "spring.cloud.gateway.router.auth";

}
