/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.gateway.utils;

import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.Set;

import static org.laokou.common.i18n.common.StringConstants.EMPTY;

/**
 * @author laokou
 */
public class ReactiveRequestUtil {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	public static String getParamValue(ServerHttpRequest request, String paramName) {
		// 从header中获取
		String paramValue = request.getHeaders().getFirst(paramName);
		// 从参数中获取
		if (StringUtil.isEmpty(paramValue)) {
			paramValue = request.getQueryParams().getFirst(paramName);
		}
		return StringUtil.isEmpty(paramValue) ? EMPTY : paramValue.trim();
	}

	public static String getRequestURL(ServerHttpRequest request) {
		return request.getPath().pathWithinApplication().value();
	}

	public static String getHost(ServerHttpRequest request) {
		return request.getURI().getHost();
	}

	public static MediaType getContentType(ServerHttpRequest request) {
		return request.getHeaders().getContentType();
	}

	public static String getMethodName(ServerHttpRequest request) {
		return request.getMethod().name();
	}

	public static boolean pathMatcher(String requestMethod, String requestURL, Map<String, Set<String>> urlMap) {
		Set<String> urls = urlMap.get(requestMethod);
		if (CollectionUtil.isEmpty(urls)) {
			return false;
		}
		for (String url : urls) {
			if (ANT_PATH_MATCHER.match(url, requestURL)) {
				return true;
			}
		}
		return false;
	}

	public static boolean pathMatcher(String requestURL, String url) {
		return ANT_PATH_MATCHER.match(url, requestURL);
	}

}
