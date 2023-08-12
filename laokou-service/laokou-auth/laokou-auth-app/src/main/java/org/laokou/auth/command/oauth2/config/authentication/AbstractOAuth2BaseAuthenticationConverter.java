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
package org.laokou.auth.command.oauth2.config.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.exception.handler.OAuth2ExceptionHandler;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.laokou.auth.common.BizCode.*;
import static org.laokou.auth.common.exception.ErrorCode.*;
import static org.laokou.auth.common.Constant.*;

/**
 * 邮件/手机/密码
 *
 * @author laokou
 */
@Slf4j
public abstract class AbstractOAuth2BaseAuthenticationConverter implements AuthenticationConverter {

	/**
	 * 类型
	 * @return String
	 */
	abstract String getGrantType();

	/**
	 * 子类实现转换
	 * @param clientPrincipal 认证参数
	 * @param additionalParameters 扩展参数
	 */
	abstract Authentication convert(Authentication clientPrincipal, Map<String, Object> additionalParameters);

	@Override
	public Authentication convert(HttpServletRequest request) {
		// 请求链 FilterOrderRegistration
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!getGrantType().equals(grantType)) {
			return null;
		}
		String traceId = request.getHeader(Constant.TRACE_ID);
		if (StringUtil.isNotEmpty(traceId)) {
			MDC.put(Constant.TRACE_ID, traceId);
		}
		// 判断租户编号是否为空
		String tenantId = request.getParameter(TENANT_ID);
		log.info("租户编号：{}", tenantId);
		if (StringUtil.isEmpty(tenantId)) {
			throw OAuth2ExceptionHandler.getException(TENANT_ID_NOT_NULL, MessageUtil.getMessage(TENANT_ID_NOT_NULL));
		}
		// 构建请求参数集合
		MultiValueMap<String, String> parameters = MapUtil.getParameters(request);
		// 判断scope
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtil.isNotEmpty(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			throw OAuth2ExceptionHandler.getException(INVALID_SCOPE, MessageUtil.getMessage(INVALID_SCOPE));
		}
		// 获取上下文认证信息
		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> additionalParameters = new HashMap<>(parameters.size());
		parameters.forEach((key, value) -> {
			if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.CLIENT_ID)) {
				additionalParameters.put(key, value.get(0));
			}
		});
		return convert(clientPrincipal, additionalParameters);
	}

}
