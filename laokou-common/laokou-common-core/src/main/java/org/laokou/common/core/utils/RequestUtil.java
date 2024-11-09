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
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.laokou.common.core.utils.IpUtil.LOCAL_IPV4;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.TraceConstant.DOMAIN_NAME;
import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * 请求工具类.
 *
 * @author laokou
 */
public final class RequestUtil {

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

	/**
	 * 获取UserAgentParser对象.
	 */
	public static UserAgentParser getUserAgentParser() {
		return PARSER;
	}

	/**
	 * 获取请求对象.
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
	 * 获取方法处理器.
	 * @param request 请求对象
	 * @param handlerMapping 映射处理器
	 */
	@SneakyThrows
	public static HandlerMethod getHandlerMethod(HttpServletRequest request, HandlerMapping handlerMapping) {
		HandlerExecutionChain chain = handlerMapping.getHandler(request);
		if (chain != null && chain.getHandler() instanceof HandlerMethod handlerMethod) {
			return handlerMethod;
		}
		return null;
	}

	/**
	 * 获取浏览器信息.
	 * @param request 请求对象
	 * @return 浏览器信息
	 */
	public static Capabilities getCapabilities(HttpServletRequest request) {
		return PARSER.parse(request.getHeader(USER_AGENT));
	}

	/**
	 * 获取参数值.
	 * @param request 请求对象
	 * @param paramName 参数名称
	 */
	public static String getParamValue(HttpServletRequest request, String paramName) {
		String paramValue = request.getHeader(paramName);
		// 从参数中获取
		if (StringUtil.isEmpty(paramValue)) {
			paramValue = request.getParameter(paramName);
		}
		return StringUtil.isEmpty(paramValue) ? EMPTY : paramValue.trim();
	}

	/**
	 * 获取请求体.
	 * @param request 请求对象
	 */
	@SneakyThrows
	public static byte[] getRequestBody(HttpServletRequest request) {
		return StreamUtils.copyToByteArray(request.getInputStream());
	}

	/**
	 * 获取流.
	 * @param requestBody 请求体
	 */
	public static ServletInputStream getInputStream(byte[] requestBody) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody);
		return new ServletInputStream() {

			@Override
			public int read() {
				return inputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				throw new UnsupportedOperationException();
			}
		};
	}

	public final static class RequestWrapper extends HttpServletRequestWrapper {

		private final byte[] REQUEST_BODY;

		/**
		 * Constructs a request object wrapping the given request.
		 * @param request the {@link HttpServletRequest} to be wrapped.
		 * @throws IllegalArgumentException if the request is null
		 */
		public RequestWrapper(HttpServletRequest request) {
			super(request);
			REQUEST_BODY = getRequestBody(request);
		}

		@Override
		public BufferedReader getReader() {
			return new BufferedReader(new ByteArrayInputStreamReader(REQUEST_BODY));
		}

		@Override
		public ServletInputStream getInputStream() {
			return RequestUtil.getInputStream(REQUEST_BODY);
		}

		private static class ByteArrayInputStreamReader extends InputStreamReader {

			public ByteArrayInputStreamReader(byte[] body) {
				super(new ByteArrayInputStream(body));
			}

		}

	}

}
