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

package org.laokou.common.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.dto.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 请求响应拦截处理器.
 *
 * @author laokou
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnClass(ResponseBodyAdvice.class)
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(@NonNull MethodParameter returnType,
			@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
			@NonNull MediaType selectedContentType,
			@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request,
			@NonNull ServerHttpResponse response) {
		// log.info("拦截响应信息：{}", JacksonUtil.toJsonStr(body));
		if (body instanceof Result<?> result) {
			return result;
		}
		if (ObjectUtils.isNull(body)) {
			return Result.ok(StringConstants.EMPTY);
		}
		return body;
	}

}
