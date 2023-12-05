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

package org.laokou.common.nacos.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.holder.ShutdownHolder;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.laokou.common.i18n.common.Constant.EMPTY;
import static org.laokou.common.i18n.common.Constant.GRACEFUL_SHUTDOWN_URL;
import static org.laokou.common.i18n.common.StatusCode.SERVICE_UNAVAILABLE;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
@WebFilter(filterName = "shutdownFilter", urlPatterns = GRACEFUL_SHUTDOWN_URL)
public class ShutdownFilter implements Filter, org.springframework.web.server.WebFilter {

	private static final ScheduledExecutorService NEWED_SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(1);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("加载优雅停机过滤器");
		Filter.super.init(filterConfig);
	}

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		if (open()) {
			ResponseUtil.response((HttpServletResponse) response, JacksonUtil.toJsonStr(Result.of(EMPTY)));
			return;
		}
		ResponseUtil.response((HttpServletResponse) response, JacksonUtil.toJsonStr(Result.fail(SERVICE_UNAVAILABLE)));
	}

	@Override
	public void destroy() {
		log.info("优雅停机执行完毕");
		Filter.super.destroy();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		if (open()) {
			return org.laokou.common.nacos.utils.ResponseUtil.response(exchange,
					JacksonUtil.toJsonStr(Result.of(EMPTY)));
		}
		return org.laokou.common.nacos.utils.ResponseUtil.response(exchange,
				JacksonUtil.toJsonStr(Result.fail(SERVICE_UNAVAILABLE)));
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
					long end = IdGenerator.SystemClock.now();
					// 一分钟内没完成 或 计数器为 -> 结束
					if (end - start >= second || ShutdownHolder.get() == 0) {
						NEWED_SCHEDULED_THREAD_POOL.shutdown();
					}
				}, 0, 1, TimeUnit.SECONDS);
			}));
			return true;
		}
		return false;
	}

}
