/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.common.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * {@link ConfigurationSettingNames}
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = OAuth2ResourceServerProperties.PREFIX)
public class OAuth2ResourceServerProperties {

	public static final String PREFIX = "spring.security.oauth2.resource-server";

	public static final String ENABLED = "enabled";

	private boolean enabled = true;

	private RequestMatcher requestMatcher;

	@Data
	public static class RequestMatcher {

		private Set<String> patterns;

	}

}
