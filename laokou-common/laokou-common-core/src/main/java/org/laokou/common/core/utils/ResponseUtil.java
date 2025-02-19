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

package org.laokou.common.core.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * 响应工具类.
 *
 * @author laokou
 */
public final class ResponseUtil {

	private ResponseUtil() {
	}

	/**
	 * 响应视图【状态码OK，json格式】.
	 * @param response 响应对象
	 * @param obj 对象
	 */
	@SneakyThrows
	public static void responseOk(HttpServletResponse response, Object obj) {
		responseOk(response, JacksonUtil.toJsonStr(obj), APPLICATION_JSON_VALUE);
	}

	/**
	 * 响应视图【状态码OK，自定义响应类型】.
	 * @param response 响应对象
	 * @param str 对象
	 */
	@SneakyThrows
	public static void responseOk(HttpServletResponse response, String str, String contentType) {
		response.setStatus(HttpStatus.OK.value());
		response(response, str, contentType);
	}

	@SneakyThrows
	private static void response(HttpServletResponse response, String str, String contentType) {
		response.setContentType(contentType);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentLength(str.getBytes(StandardCharsets.UTF_8).length);
		try (PrintWriter writer = response.getWriter()) {
			writer.write(str);
			writer.flush();
		}
	}

	/**
	 * 获取响应对象.
	 * @return 请求对象
	 */
	public static HttpServletResponse getHttpServletResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.notNull(requestAttributes, "requestAttributes not be null");
		return ((ServletRequestAttributes) requestAttributes).getResponse();
	}

}
