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

package org.laokou.common.core.utils;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.laokou.common.i18n.common.TraceConstant.DOMAIN_NAME;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 请求工具类.
 *
 * @author laokou
 */
public class RequestUtil {

	/**
	 * 获取请求对象.
	 * @return 请求对象
	 */
	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.notNull(requestAttributes, "requestAttributes not be null");
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * 根据请求获取域名.
	 * @param request 请求对象
	 * @return 域名
	 */
	public static String getDomainName(HttpServletRequest request) {
		return request.getHeader(DOMAIN_NAME);
	}

	/**
	 * 获取浏览器信息.
	 * @param request 请求对象
	 * @return 浏览器信息
	 */
	public static UserAgent getUserAgent(HttpServletRequest request) {
		return UserAgent.parseUserAgentString(request.getHeader(USER_AGENT));
	}

}
