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
package org.laokou.common.security.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 无效认证
 *
 * @author laokou
 */
@Slf4j
@Component
public class InvalidAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		if (authException instanceof InsufficientAuthenticationException) {
			OAuth2ExceptionHandler.response(response, StatusCode.UNAUTHORIZED,
					MessageUtil.getMessage(StatusCode.UNAUTHORIZED));
		}
		if (authException instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
			String message = oAuth2AuthenticationException.getError().getDescription();
			int errorCode = Integer.parseInt(oAuth2AuthenticationException.getError().getErrorCode());
			OAuth2ExceptionHandler.response(response, errorCode, message);
		}
	}

}
