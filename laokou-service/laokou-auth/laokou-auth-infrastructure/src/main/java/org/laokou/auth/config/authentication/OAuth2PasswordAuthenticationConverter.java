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

package org.laokou.auth.config.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Map;

/**
 * 密码转换器.
 *
 * @author laokou
 */
public class OAuth2PasswordAuthenticationConverter extends AbstractOAuth2AuthenticationConverter {

	@Override
	String getGrantType() {
		return OAuth2ParameterNames.PASSWORD;
	}

	@Override
	Authentication convert(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
		return new OAuth2PasswordAuthenticationToken(clientPrincipal, additionalParameters);
	}

}
