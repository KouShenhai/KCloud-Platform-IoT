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

package org.laokou.auth.service.authentication;

import org.laokou.auth.model.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;


/**
 * 邮箱令牌.
 *
 * @author laokou
 */
final class OAuth2MailAuthenticationToken extends AbstractOAuth2AuthenticationToken {

	/**
	 * Subclass constructor.
	 * @param clientPrincipal the authenticated client principal
	 * @param additionalParameters the additional parameters
	 */
	OAuth2MailAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
		super(new AuthorizationGrantType(Constants.MAIL), clientPrincipal, additionalParameters);
	}

}
