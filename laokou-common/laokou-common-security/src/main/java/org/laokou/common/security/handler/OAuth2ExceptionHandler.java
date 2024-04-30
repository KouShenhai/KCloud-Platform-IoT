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

package org.laokou.common.security.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.common.StatusCode;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.MessageUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author laokou
 */
public class OAuth2ExceptionHandler {

	@Schema(name = "ERROR_URL", description = "错误地址")
	public static final String ERROR_URL = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	public static OAuth2AuthenticationException getException(String code, String message, String uri) {
		return new OAuth2AuthenticationException(new OAuth2Error(code, message, uri));
	}

	public static OAuth2AuthenticationException getException(String code, String uri) {
		return getException(code, MessageUtil.getMessage(code), uri);
	}

	public static OAuth2AuthenticationException getException(String code) {
		return getException(code, MessageUtil.getMessage(code), ERROR_URL);
	}

	@SneakyThrows
	public static void handleAccessDenied(HttpServletResponse response, Throwable ex) {
		if (ex instanceof AccessDeniedException) {
			ResponseUtil.response(response, Result.fail(StatusCode.FORBIDDEN));
		}
	}

	@SneakyThrows
	public static void handleAuthentication(HttpServletResponse response, Throwable ex) {
		if (ex instanceof OAuth2AuthenticationException authenticationException) {
			String msg = authenticationException.getError().getDescription();
			String code = authenticationException.getError().getErrorCode();
			ResponseUtil.response(response, Result.fail(code, msg));
			return;
		}
		if (ex instanceof InsufficientAuthenticationException) {
			ResponseUtil.response(response, Result.fail(StatusCode.UNAUTHORIZED));
		}
	}

}
