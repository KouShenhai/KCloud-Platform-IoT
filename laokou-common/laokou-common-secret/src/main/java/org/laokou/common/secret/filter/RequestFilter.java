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

package org.laokou.common.secret.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.annotation.Order;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import static org.laokou.common.core.utils.RequestUtil.getRequestBody;

/**
 * @author laokou
 */
@Order(10000)
@AutoConfiguration
@WebFilter(filterName = "RequestFilter", urlPatterns = "/**")
public class RequestFilter implements Filter {

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) {
		ServletRequest requestWrapper = null;
		if (servletRequest instanceof HttpServletRequest request) {
			requestWrapper = new RequestWrapper(request);
		}
		if (ObjectUtil.isNull(requestWrapper)) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			chain.doFilter(requestWrapper, servletResponse);
		}
	}

	public static class RequestWrapper extends HttpServletRequestWrapper {

		private final byte[] REQUEST_BODY;

		/**
		 * Constructs a request object wrapping the given request.
		 *
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

		private static class ByteArrayInputStreamReader extends InputStreamReader {

			public ByteArrayInputStreamReader(byte[] body) {
				super(new ByteArrayInputStream(body));
			}
		}

		@Override
		public ServletInputStream getInputStream() {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(REQUEST_BODY);
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

	}

}
