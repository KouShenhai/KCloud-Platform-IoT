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

package org.laokou.common.reactor.util;

import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.Set;

/**
 * 响应式请求工具类.
 *
 * @author laokou
 */
public class ReactiveRequestUtils {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * 获取参数值.
	 * @param request 请求对象
	 * @param paramName 请求参数名称
	 * @return 参数值
	 */
	public static String getParamValue(ServerHttpRequest request, String paramName) {
		// 从header中获取
		String paramValue = request.getHeaders().getFirst(paramName);
		// 从参数中获取
		if (StringExtUtils.isEmpty(paramValue)) {
			paramValue = request.getQueryParams().getFirst(paramName);
		}
		return StringExtUtils.isEmpty(paramValue) ? "" : paramValue.trim();
	}

	/**
	 * 获取请求路径URL.
	 * @param request 请求对象
	 * @return 路径URL
	 */
	public static String getRequestURL(ServerHttpRequest request) {
		return request.getPath().pathWithinApplication().value();
	}

	/**
	 * 获取主机.
	 * @param request 请求对象
	 * @return 主机
	 */
	public static String getHost(ServerHttpRequest request) {
		return request.getURI().getHost();
	}

	/**
	 * 获取请求格式.
	 * @param request 请求对象
	 * @return 请求格式
	 */
	public static MediaType getContentType(ServerHttpRequest request) {
		String value = request.getHeaders().getFirst("content-type");
		return org.springframework.util.StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null;
	}

	/**
	 * 获取请求方法.
	 * @param request 请求对象
	 * @return 请求方法
	 */
	public static String getMethodName(ServerHttpRequest request) {
		return request.getMethod().name();
	}

	/**
	 * 路径匹配.
	 * @param requestMethod 请求方法
	 * @param requestURL 请求路径URL
	 * @param uriMap uri集合
	 * @return 匹配结果
	 */
	public static boolean pathMatcher(String requestMethod, String requestURL, Map<String, Set<String>> uriMap) {
		Set<String> urls = uriMap.get(requestMethod);
		if (CollectionUtils.isEmpty(urls)) {
			return false;
		}
		for (String url : urls) {
			if (pathMatcher(url, requestURL)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 路径匹配.
	 * @param requestURL 请求路径URL
	 * @param url 路径URL
	 * @return 匹配结果
	 */
	private static boolean pathMatcher(String url, String requestURL) {
		return ANT_PATH_MATCHER.match(url, requestURL);
	}

}
