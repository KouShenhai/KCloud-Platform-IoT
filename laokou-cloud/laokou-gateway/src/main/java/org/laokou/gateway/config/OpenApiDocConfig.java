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

package org.laokou.gateway.config;

import lombok.SneakyThrows;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.constants.StringConstant.NULL;
import static org.laokou.common.i18n.common.constants.StringConstant.SLASH;
import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

/**
 * API文档配置.
 *
 * @author laokou
 */
@Configuration
public class OpenApiDocConfig {

	/**
	 * https协议。
	 */
	private static final String HTTPS_PROTOCOL = "https";

	@Bean
	@Lazy(false)
	@SneakyThrows
	public Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> openApis(RouteDefinitionLocator locator,
			ServerProperties serverProperties, SwaggerUiConfigParameters swaggerUiConfigParameters) {
		Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		assert definitions != null;
		definitions.stream().filter(routeDefinition -> {
			if (!serverProperties.getSsl().isEnabled() && HTTPS_PROTOCOL.equals(routeDefinition.getUri().getScheme())) {
				throw new RuntimeException(
						String.format("HTTP不允许开启SSL，请检查URL为%s的路由", routeDefinition.getUri().toString()));
			}
			return routeDefinition.getId().matches("laokou-.*");
		}).forEach(routeDefinition -> {
			String name = routeDefinition.getId().substring(7);
			AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(
					name, DEFAULT_API_DOCS_URL + SLASH + name, NULL);
			urls.add(swaggerUrl);
		});
		swaggerUiConfigParameters.setUrls(urls);
		return urls;
	}

}
