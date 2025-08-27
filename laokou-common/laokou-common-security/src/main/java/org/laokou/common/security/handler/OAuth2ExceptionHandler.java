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

package org.laokou.common.security.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.core.util.ResponseUtils;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.MessageUtils;
import org.laokou.common.reactor.util.ReactiveResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author laokou
 */
public class OAuth2ExceptionHandler {

	public static final String ERROR_URL = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	public static OAuth2AuthenticationException getOAuth2AuthenticationException(String code, String message,
			String uri) {
		return new OAuth2AuthenticationException(new OAuth2Error(code, message, uri));
	}

	public static OAuth2AuthenticationException getException(String code, String uri) {
		return getOAuth2AuthenticationException(code, MessageUtils.getMessage(code), uri);
	}

	public static OAuth2AuthenticationException getException(String code) {
		return getOAuth2AuthenticationException(code, MessageUtils.getMessage(code), ERROR_URL);
	}

	public static void handleAccessDenied(HttpServletResponse response, Throwable ex) throws IOException {
		if (ex instanceof AccessDeniedException) {
			ResponseUtils.responseOk(response, Result.fail(StatusCode.FORBIDDEN));
		}
	}

	public static void handleAuthentication(HttpServletResponse response, Throwable ex) throws IOException {
		if (ex instanceof OAuth2AuthenticationException authenticationException) {
			OAuth2Error error = authenticationException.getError();
			String code = error.getErrorCode();
			String msg = error.getDescription();
			ResponseUtils.responseOk(response, Result.fail(code, msg));
			return;
		}
		if (ex instanceof InsufficientAuthenticationException) {
			ResponseUtils.responseOk(response, Result.fail(StatusCode.UNAUTHORIZED));
		}
	}

	public static Mono<Void> handleAccessDenied(ServerWebExchange exchange, Throwable ex) {
		if (ex instanceof AccessDeniedException) {
			return ReactiveResponseUtils.responseOk(exchange, Result.fail(StatusCode.FORBIDDEN));
		}
		return Mono.error(ex);
	}

	public static Mono<Void> handleAuthentication(ServerWebExchange exchange, Throwable ex) {
		if (ex instanceof OAuth2AuthenticationException authenticationException) {
			OAuth2Error error = authenticationException.getError();
			String code = error.getErrorCode();
			String msg = error.getDescription();
			return ReactiveResponseUtils.responseOk(exchange, Result.fail(code, msg));
		}
		if (ex instanceof InsufficientAuthenticationException) {
			return ReactiveResponseUtils.responseOk(exchange, Result.fail(StatusCode.UNAUTHORIZED));
		}
		return Mono.error(ex);
	}

}
