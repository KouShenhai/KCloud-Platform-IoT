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

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.core.annotation.Order;

/**
 * @author laokou
 */
@Order(10000)
@WebFilter(filterName = "RequestFilter", urlPatterns = "/**")
public class RequestFilter implements Filter {

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) {
		ServletRequest requestWrapper = null;
		if (servletRequest instanceof HttpServletRequest request) {
			requestWrapper = new RequestUtil.RequestWrapper(request);
		}
		if (ObjectUtil.isNull(requestWrapper)) {
			chain.doFilter(servletRequest, servletResponse);
		}
		else {
			chain.doFilter(requestWrapper, servletResponse);
		}
	}

}
