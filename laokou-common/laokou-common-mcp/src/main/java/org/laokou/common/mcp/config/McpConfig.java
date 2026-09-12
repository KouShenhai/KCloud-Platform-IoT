/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.mcp.config;

import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.core.config.OAuth2AuthorizedToken;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.springframework.ai.mcp.customizer.McpClientCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * @author laokou
 */
@Configuration(proxyBeanMethods = false)
public class McpConfig {

	@Bean
	public McpClientCustomizer<HttpClientStreamableHttpTransport.Builder> mcpAuthorizationCustomizer(
			ObjectProvider<OAuth2AuthorizedToken> objectProvider) {
		return (_, builder) -> builder.httpRequestCustomizer(
				request -> request.setHeader(HttpHeaders.AUTHORIZATION, getAccessToken(objectProvider)));
	}

	private String getAccessToken(ObjectProvider<OAuth2AuthorizedToken> objectProvider) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(authorization) && authorization.startsWith(StringConstants.BEARER_PREFIX)) {
			return authorization;
		}
		return StringConstants.BEARER_PREFIX + objectProvider.getObject().getAccessToken();
	}

}
