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

package org.laokou.common.xss.filter;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.ArrayUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.xss.util.XssUtil;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.*;

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
	@SneakyThrows
	public ServletInputStream getInputStream() {
		if (!checkJson()) {
			return super.getInputStream();
		}
		// 请求为空直接返回
		byte[] requestBody = RequestUtil.getRequestBody(request);
		String json = new String(requestBody, StandardCharsets.UTF_8);
		if (StringUtil.isEmpty(json)) {
			return super.getInputStream();
		}
		// xss过滤
		return RequestUtil.getInputStream(XssUtil.clear(json).getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String getParameter(String name) {
		String parameterValue = super.getParameter(name);
		if (StringUtil.isNotEmpty(parameterValue)) {
			parameterValue = XssUtil.clear(parameterValue);
		}
		return parameterValue;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] parameterValues = super.getParameterValues(name);
		if (ArrayUtil.isEmpty(parameterValues)) {
			return new String[0];
		}
		List<String> list = new ArrayList<>(parameterValues.length);
		for (String parameterValue : parameterValues) {
			list.add(XssUtil.clear(parameterValue));
		}
		return list.toArray(String[]::new);
	}

	/**
	 * @see DefaultMultipartHttpServletRequest#getParameterMap().
	 * @return Map<String,String[]>
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> newParameterMap = new LinkedHashMap<>();
		Map<String, String[]> parameterMap = super.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			List<String> list = new ArrayList<>(values.length);
			for (String str : values) {
				list.add(XssUtil.clear(str));
			}
			newParameterMap.put(key, list.toArray(String[]::new));
		}
		return newParameterMap;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(XssUtil.clear(name));
		if (StringUtil.isNotEmpty(value)) {
			value = XssUtil.clear(value);
		}
		return value;
	}

	private boolean checkJson() {
		String header = super.getHeader(CONTENT_TYPE);
		return StringUtil.startWith(header, APPLICATION_JSON_VALUE);
	}

}
