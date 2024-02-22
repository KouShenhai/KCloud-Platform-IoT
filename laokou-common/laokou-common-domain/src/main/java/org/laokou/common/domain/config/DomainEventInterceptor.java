/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.domain.config;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.domain.context.DomainEventContextHolder;
import org.laokou.common.domain.publish.DomainEventPublisher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static org.laokou.common.i18n.common.JobModeEnums.SYNC;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
@AutoConfiguration
@RequiredArgsConstructor
public class DomainEventInterceptor implements HandlerInterceptor {

	private final DomainEventPublisher domainEventPublisher;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) {
		// 发布当前线程的领域事件(同步发布)
		domainEventPublisher.publish(SYNC);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) {
		// 清除领域事件上下文
		DomainEventContextHolder.clear();
	}

}
