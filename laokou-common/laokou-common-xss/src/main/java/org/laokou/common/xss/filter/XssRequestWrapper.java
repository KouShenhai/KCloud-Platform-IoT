/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.xss.filter;

import com.google.common.net.HttpHeaders;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.xss.util.XssUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author laokou
 */
public final class XssRequestWrapper extends HttpServletRequestWrapper {

	private final HttpServletRequest request;

	/**
	 * Constructs a request object wrapping the given request.
	 * @param request the {@link HttpServletRequest} to be wrapped.
	 * @throws IllegalArgumentException if the request is null
	 */
	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (!checkJson()) {
			return super.getInputStream();
		}
		// 请求为空直接返回
		byte[] requestBody = RequestUtils.getRequestBody(request);
		String json = new String(requestBody, StandardCharsets.UTF_8);
		if (StringExtUtils.isEmpty(json)) {
			return super.getInputStream();
		}
		// xss过滤
		return RequestUtils.getInputStream(XssUtils.clearHtml(json).getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String getParameter(String name) {
		String parameterValue = super.getParameter(name);
		if (StringExtUtils.isNotEmpty(parameterValue)) {
			parameterValue = XssUtils.clearHtml(parameterValue);
		}
		return parameterValue;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] parameterValues = super.getParameterValues(name);
		if (ArrayUtils.isEmpty(parameterValues)) {
			return new String[0];
		}
		return Stream.of(parameterValues).map(XssUtils::clearHtml).toArray(String[]::new);
	}

	/**
	 * @see org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest#getParameterMap().
	 * @return Map<String,String[]>
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parameterMap = super.getParameterMap();
		Map<String, String[]> newParameterMap = MapUtils.newLinkedHashMap(parameterMap.size());
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			newParameterMap.put(key, Stream.of(values).map(XssUtils::clearHtml).toList().toArray(String[]::new));
		}
		return newParameterMap;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(XssUtils.clearHtml(name));
		if (StringExtUtils.isNotEmpty(value)) {
			value = XssUtils.clearHtml(value);
		}
		return value;
	}

	private boolean checkJson() {
		String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
		return StringExtUtils.startWith(header, MediaType.APPLICATION_JSON_VALUE);
	}

}
