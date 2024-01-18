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

package org.laokou.auth.common.exception.handler;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import static org.laokou.common.i18n.common.OAuth2Constants.ERROR_URL;

/**
 * @author laokou
 */
public class OAuth2ExceptionHandler {

	public static OAuth2AuthenticationException getException(String errorCode, String description, String uri) {
		OAuth2Error error = new OAuth2Error(errorCode, description, uri);
		return new OAuth2AuthenticationException(error);
	}

	public static OAuth2AuthenticationException getException(int errorCode, String description) {
		return getException(String.valueOf(errorCode), description, ERROR_URL);
	}

	public static OAuth2AuthenticationException getException(String errorCode, String description) {
		return getException(errorCode, description, ERROR_URL);
	}

}
