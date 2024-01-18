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

package org.laokou.common.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.common.StatusCodes;
import org.laokou.common.i18n.utils.MessageUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import static org.laokou.common.i18n.common.OAuth2Constants.ERROR_URL;

/**
 * @author laokou
 */
public class OAuth2ExceptionHandler {

	public static OAuth2AuthenticationException getException(String errorCode, String description, String uri) {
		return new OAuth2AuthenticationException(new OAuth2Error(errorCode, description, uri));
	}

	public static OAuth2AuthenticationException getException(int errorCode, String description) {
		return getException(String.valueOf(errorCode), description, ERROR_URL);
	}

	@SneakyThrows
	public static void handleAccessDenied(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		if (ex instanceof AccessDeniedException) {
			ResponseUtil.response(response, StatusCodes.FORBIDDEN, MessageUtil.getMessage(StatusCodes.FORBIDDEN));
		}
	}

	@SneakyThrows
	public static void handleAuthentication(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		if (ex instanceof OAuth2AuthenticationException authenticationException) {
			String message = authenticationException.getError().getDescription();
			int errorCode = Integer.parseInt(authenticationException.getError().getErrorCode());
			ResponseUtil.response(response, errorCode, message);
			return;
		}
		if (ex instanceof InsufficientAuthenticationException) {
			ResponseUtil.response(response, StatusCodes.UNAUTHORIZED, MessageUtil.getMessage(StatusCodes.UNAUTHORIZED));
		}
	}

}
