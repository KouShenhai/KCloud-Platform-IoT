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

package org.laokou.common.security.config;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.security.utils.UserDetail;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.laokou.common.i18n.common.constants.Constant.AUTHORIZATION;

/**
 * @author laokou
 */
@AutoConfiguration
@NonNullApi
public class UserContextInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		UserContextHolder.set(convert(UserUtil.user(), request));
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) {
		UserContextHolder.clear();
	}

	private UserContextHolder.User convert(UserDetail userDetail, HttpServletRequest request) {
		return new UserContextHolder.User(userDetail.getId(), userDetail.getUsername(), userDetail.getTenantId(),
				userDetail.getDeptPath(), userDetail.getDeptId(), userDetail.getSourceName(),
				request.getHeader(AUTHORIZATION).substring(7));
	}

}
