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

package org.laokou.gateway.repository;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.nacos.util.ConfigUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

import static org.laokou.gateway.constant.GatewayConstants.ROUTER_NOT_EXIST;

// @formatter:off
/**
 * nacos动态路由缓存库.
 * <a href="https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/src/main/java/com/alibaba/cloud/examples/example/ConfigListenerExample.java">nacos拉取配置</a>
 *
 * @author laokou
 */
// @formatter:on
@Slf4j
@Component
@NonNullApi
@Repository
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware {

	/**
	 * 动态路由配置.
	 */
	private static final String DATA_ID = "router.json";

	private final ConfigUtils configUtils;

	private final ReactiveHashOperations<String, String, RouteDefinition> reactiveHashOperations;

	private ApplicationEventPublisher applicationEventPublisher;

	public NacosRouteDefinitionRepository(ConfigUtils configUtils,
			ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
		this.configUtils = configUtils;
		this.reactiveHashOperations = reactiveRedisTemplate.opsForHash();
	}

	// @formatter:off
	/**
	 * 路由基本原理总结：
	 * 1.从NacosRouteDefinitionRepository、DiscoveryClientRouteDefinitionLocator和PropertiesRouteDefinitionLocator加载定义的路由规则.
	 * 2.通过CompositeRouteDefinitionLocator合并定义的路由规则.
	 * 3.加载所有的定义的路由规则，使用配置的断言工厂和过滤器工厂来创建路由.
	 * 4.将路由缓存，提高路由查找性能.
	 * <p>
	 * 获取动态路由（避免集群中网关频繁调用Redis，需要本地缓存）.
	 * {@link org.springframework.cloud.gateway.config.GatewayAutoConfiguration
	 * @return 定义的路由规则
	 */
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		return reactiveHashOperations.entries(RedisKeyUtils.getRouteDefinitionHashKey())
			.mapNotNull(Map.Entry::getValue)
			.onErrorContinue((throwable, routeDefinition) -> {
				if (log.isErrorEnabled()) {
					log.error("Get routes from redis error cause : {}", throwable.toString(), throwable);
				}
			});
	}
	// @formatter:on

	// @formatter:off
	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return Mono.empty();
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return Mono.empty();
	}

	/**
	 * 保存路由【同步Nacos路由配置到Redis】.
	 * @return 保存结果
	 */
	public Flux<Boolean> saveRouters() {
		return Flux.fromIterable(pullRouters())
			.flatMap(router -> reactiveHashOperations.putIfAbsent(RedisKeyUtils.getRouteDefinitionHashKey(), router.getId(), router)).doFinally(c -> refreshEvent());
	}

	/**
	 * 删除路由.
	 * @return 删除结果
	 */
	public Mono<Boolean> removeRouters() {
		return reactiveHashOperations.delete(RedisKeyUtils.getRouteDefinitionHashKey()).doFinally(c -> refreshEvent());
	}

	/**
	 * 拉取nacos动态路由配置.
	 * @return 拉取结果
	 */
	private Collection<RouteDefinition> pullRouters() {
		try {
			// pull nacos config info
			String group = configUtils.getGroup();
			String configInfo = configUtils.getConfig(DATA_ID, group, 5000);
			return JacksonUtils.toList(configInfo, RouteDefinition.class);
		}
		catch (Exception e) {
			log.error("路由不存在，错误信息：{}", e.getMessage());
			throw new SystemException(ROUTER_NOT_EXIST);
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * 刷新事件.
	 */
	private void refreshEvent() {
		// 刷新事件
		applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
	}
	// @formatter:on

}
