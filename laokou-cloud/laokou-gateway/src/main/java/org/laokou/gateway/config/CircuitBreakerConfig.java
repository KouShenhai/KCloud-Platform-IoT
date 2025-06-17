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

package org.laokou.gateway.config;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.apache.hc.core5.http.HttpStatus;
import org.laokou.common.i18n.dto.Result;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Duration;

import static org.laokou.common.i18n.common.exception.StatusCode.SERVICE_UNAVAILABLE;

/**
 * @author laokou
 */
@Configuration
public class CircuitBreakerConfig {

	@Bean
	public RouterFunction<ServerResponse> routerFunction() {
		return RouterFunctions.route(
				RequestPredicates.path("/fallback").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
				(request) -> ServerResponse.status(HttpStatus.SC_OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(Result.fail(SERVICE_UNAVAILABLE))));
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> reactiveResilience4JCircuitBreakerFactoryCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
			// 15秒后超时时间
			.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(15)).build())
			.circuitBreakerConfig(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
				// 失败率阈值调至60%（超过则开启断路器）
				.failureRateThreshold(60)
				// 慢调用率阈值60%（超过则开启断路器）
				.slowCallRateThreshold(60)
				// 慢调用判定时间5s
				.slowCallDurationThreshold(Duration.ofSeconds(5))
				// 断路器开启25秒后进入半开状态
				.waitDurationInOpenState(Duration.ofSeconds(25))

				// 基于时间的滑动窗口
				.slidingWindowType(
						io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
				// 10秒统计窗口
				.slidingWindowSize(10)
				// 窗口内至少30次调用才计算阈值
				.minimumNumberOfCalls(30)

				// 半开状态下允许15次调用
				.permittedNumberOfCallsInHalfOpenState(15)
				// 半开状态最大等待3秒
				.maxWaitDurationInHalfOpenState(Duration.ofSeconds(3))

				// 自动切换到半开
				.automaticTransitionFromOpenToHalfOpenEnabled(true)
				// 禁用堆栈跟踪提升性能
				.writableStackTraceEnabled(false)

				.build())
			.build());
	}

}
