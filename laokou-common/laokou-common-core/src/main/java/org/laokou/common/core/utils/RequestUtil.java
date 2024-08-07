/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.blueconic.browscap.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Arrays;

import static org.laokou.common.core.utils.IpUtil.LOCAL_IPV4;
import static org.laokou.common.i18n.common.constant.TraceConstant.DOMAIN_NAME;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 请求工具类.
 *
 * @author laokou
 */
public class RequestUtil {

	private static final UserAgentParser PARSER;

	static {
		try {
			PARSER = new UserAgentService().loadParser(Arrays.asList(BrowsCapField.BROWSER, BrowsCapField.BROWSER_TYPE,
					BrowsCapField.BROWSER_MAJOR_VERSION, BrowsCapField.DEVICE_TYPE, BrowsCapField.PLATFORM,
					BrowsCapField.PLATFORM_VERSION, BrowsCapField.RENDERING_ENGINE_VERSION,
					BrowsCapField.RENDERING_ENGINE_NAME, BrowsCapField.PLATFORM_MAKER,
					BrowsCapField.RENDERING_ENGINE_MAKER));
		}
		catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static UserAgentParser getUserAgentParser() {
		return PARSER;
	}

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
		String domainName = request.getHeader(DOMAIN_NAME);
		return StringUtil.isEmpty(domainName) ? LOCAL_IPV4 : domainName;
	}

	/**
	 * 获取浏览器信息.
	 * @param request 请求对象
	 * @return 浏览器信息
	 */
	public static Capabilities getCapabilities(HttpServletRequest request) {
		return PARSER.parse(request.getHeader(USER_AGENT));
	}

	@SneakyThrows
	public static byte[] getRequestBody(HttpServletRequest request) {
		return StreamUtils.copyToByteArray(request.getInputStream());
	}

}
