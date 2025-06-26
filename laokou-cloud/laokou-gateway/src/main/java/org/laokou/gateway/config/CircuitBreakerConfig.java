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

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;
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
			// 超时时间
			.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(15)).build())
			.circuitBreakerConfig(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
				// 失败率阈值（超过则开启断路器）
				.failureRateThreshold(50)
				// 慢调用率阈值（超过则开启断路器）
				.slowCallRateThreshold(60)
				// 慢调用判定时间
				.slowCallDurationThreshold(Duration.ofSeconds(5))
				// 断路器开启后等待时间
				.waitDurationInOpenState(Duration.ofSeconds(5))

				// 基于时间的滑动窗口
				.slidingWindowType(COUNT_BASED)
				// 统计窗口大小
				.slidingWindowSize(30)
				// 窗口内计算阈值的调用次数
				.minimumNumberOfCalls(20)

				// 半开状态下允许调用次数
				.permittedNumberOfCallsInHalfOpenState(15)
				// 半开状态最大等待时间
				.maxWaitDurationInHalfOpenState(Duration.ofSeconds(5))

				// 关闭自动切换到半开
				.automaticTransitionFromOpenToHalfOpenEnabled(false)
				// 禁用堆栈跟踪提升性能
				.writableStackTraceEnabled(false)

				.build())
			.build());
	}

}
