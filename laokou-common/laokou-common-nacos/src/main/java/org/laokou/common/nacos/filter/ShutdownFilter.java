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

package org.laokou.common.nacos.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.nacos.context.ShutdownHolder;
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.core.util.ResponseUtils;
import org.laokou.common.core.util.SpringContextUtils;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.nacos.util.ReactiveResponseUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
import static org.laokou.common.i18n.common.exception.StatusCode.SERVICE_UNAVAILABLE;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
@RequiredArgsConstructor
@WebFilter(filterName = "shutdownFilter", urlPatterns = "/graceful-shutdown")
public class ShutdownFilter implements Filter, org.springframework.web.server.WebFilter {

	private static final ScheduledExecutorService NEWED_SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(1);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("加载优雅停机过滤器");
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
		if (open()) {
			ResponseUtils.responseOk((HttpServletResponse) response, Result.ok(EMPTY));
			return;
		}
		ResponseUtils.responseOk((HttpServletResponse) response, Result.fail(SERVICE_UNAVAILABLE));
	}

	@Override
	public void destroy() {
		log.info("优雅停机执行完毕");
		Filter.super.destroy();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		if (open()) {
			return ReactiveResponseUtils.responseOk(exchange, Result.ok(EMPTY));
		}
		return ReactiveResponseUtils.responseOk(exchange, Result.fail(SERVICE_UNAVAILABLE));
	}

	private boolean open() {
		// 打开挡板（直接响应前端 -> 服务正在维护）
		if (ShutdownHolder.open()) {
			// 注册关闭钩子函数
			log.info("钩子函数注册成功");
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				int second = 60 * 1000;
				long start = IdGenerator.SystemClock.now();
				NEWED_SCHEDULED_THREAD_POOL.scheduleWithFixedDelay(() -> {
					// 一分钟内没完成 或 计数器为0 -> 结束
					if (IdGenerator.SystemClock.now() - start >= second || ShutdownHolder.get() == 0) {
						ThreadUtils.shutdown(NEWED_SCHEDULED_THREAD_POOL, 30);
						log.info("关闭应用");
						int exitCode = SpringApplication.exit(SpringContextUtils.getApplicationContext(),
								new ExitCodeGeneratorImpl());
						System.exit(exitCode);
					}
				}, 0, 1, TimeUnit.SECONDS);
			}));
			return true;
		}
		return false;
	}

}
