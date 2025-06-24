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

package org.laokou.auth.config;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.model.GrantTypeEnum;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static org.laokou.auth.factory.DomainFactory.*;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
public class UsernamePasswordFilter extends OncePerRequestFilter {

	public static final UsernamePasswordFilter INSTANCE = new UsernamePasswordFilter();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		RequestUtils.RequestWrapper requestWrapper = new RequestUtils.RequestWrapper(request);
		String grantType = requestWrapper.getParameter(GRANT_TYPE);
		String username = requestWrapper.getParameter(USERNAME);
		String password = requestWrapper.getParameter(PASSWORD);
		Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
		if (ObjectUtils.equals(GrantTypeEnum.USERNAME_PASSWORD.getCode(), grantType)) {
			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
				try {
					String key = entry.getKey();
					if (ObjectUtils.equals(USERNAME, key) && StringUtils.isNotEmpty(username)) {
						parameterMap.put(USERNAME, new String[]{RSAUtils.decryptByPrivateKey(username)});
					}
					if (ObjectUtils.equals(PASSWORD, key) && StringUtils.isNotEmpty(password)) {
						parameterMap.put(PASSWORD, new String[]{RSAUtils.decryptByPrivateKey(password)});
					}
				} catch (Exception e) {
					log.error("用户名密码RSA解密失败【用户名密码认证模式】，错误信息：{}", e.getMessage());
					throw new BizException("B_OAuth2_RSADecryptionFailed", "RSA解密失败", e);
				}
			}
		}
		chain.doFilter(requestWrapper, response);
	}

}
