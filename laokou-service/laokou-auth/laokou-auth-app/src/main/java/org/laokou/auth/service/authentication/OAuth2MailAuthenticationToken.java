/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;

import static org.laokou.auth.domain.model.auth.AuthA.MAIL;

/**
 * 邮箱令牌.
 *
 * @author laokou
 */
public class OAuth2MailAuthenticationToken extends AbstractOAuth2AuthenticationToken {

	/**
	 * Sub-class constructor.
	 * @param clientPrincipal the authenticated client principal
	 * @param additionalParameters the additional parameters
	 */
	protected OAuth2MailAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
		super(new AuthorizationGrantType(MAIL), clientPrincipal, additionalParameters);
	}

}
