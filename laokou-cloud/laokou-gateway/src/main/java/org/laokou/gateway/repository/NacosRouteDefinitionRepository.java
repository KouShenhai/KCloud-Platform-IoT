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

import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.SpringContextUtils;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.nacos.util.ConfigUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
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
@NonNullApi
@Repository
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

	static {
		ForyFactory.INSTANCE.register(org.springframework.cloud.gateway.route.RouteDefinition.class);
		ForyFactory.INSTANCE.register(org.springframework.cloud.gateway.filter.FilterDefinition.class);
		ForyFactory.INSTANCE.register(org.springframework.cloud.gateway.handler.predicate.PredicateDefinition.class);
	}

	private final String dataId = "router.json";

	private final ConfigUtils configUtils;

	private final ReactiveHashOperations<String, String, RouteDefinition> reactiveHashOperations;

	private final ExecutorService virtualThreadExecutor;

	public NacosRouteDefinitionRepository(ConfigUtils configUtils,
										  ReactiveRedisTemplate<String, Object> reactiveRedisTemplate,
										  ExecutorService virtualThreadExecutor) {
		this.configUtils = configUtils;
		this.reactiveHashOperations = reactiveRedisTemplate.opsForHash();
		this.virtualThreadExecutor = virtualThreadExecutor;
	}

	@PostConstruct
	public void listenRouter() throws NacosException {
		configUtils.addListener(dataId, configUtils.getGroup(), new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String routes) {
				log.info("监听路由配置信息，开始同步路由配置：{}", routes);
				virtualThreadExecutor.execute(() -> syncRouter(getRoutes(routes))
					.subscribeOn(Schedulers.boundedElastic())
					.subscribe());
			}
		});
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
					log.error("从Redis获取路由失败，错误信息：{}", throwable.getMessage(), throwable);
				}
			});
	}
	// @formatter:on

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return Mono.empty();
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return Mono.empty();
	}

	/**
	 * 同步路由【同步Nacos动态路由配置到Redis，并且刷新本地缓存】.
	 * @return 同步结果
	 */
	public Mono<Void> syncRouter() {
		return syncRouter(getRoutes());
	}

	/**
	 * 同步路由【同步Nacos动态路由配置到Redis，并且刷新本地缓存】.
	 * @param routes 路由
	 * @return 同步结果
	 */
	private Mono<Void> syncRouter(Collection<RouteDefinition> routes) {
		return reactiveHashOperations.delete(RedisKeyUtils.getRouteDefinitionHashKey())
			.doOnError(throwable -> log.error("删除路由失败，错误信息：{}", throwable.getMessage(), throwable))
			.doOnSuccess(removeFlag -> publishRefreshRoutesEvent())
			.thenMany(Flux.fromIterable(routes))
			.flatMap(router -> reactiveHashOperations.putIfAbsent(RedisKeyUtils.getRouteDefinitionHashKey(), router.getId(), router)
				.doOnError(throwable -> log.error("保存路由失败，错误信息：{}", throwable.getMessage(), throwable)))
			.then()
			.doOnSuccess(saveFlag -> publishRefreshRoutesEvent());
	}

	// @formatter:off
	/**
	 * 获取nacos动态路由配置.
	 * @return 拉取结果
	 */
	private Collection<RouteDefinition> getRoutes() {
		return getRoutes(EMPTY);
	}

	/**
	 * 获取nacos动态路由配置.
	 * @param str 路由配置
	 * @return 拉取结果
	 */
	private Collection<RouteDefinition> getRoutes(String str) {
		try {
			String routes = StringUtils.isEmpty(str) ? configUtils.getConfig(dataId, configUtils.getGroup(), 5000) : str;
			return JacksonUtils.toList(routes, RouteDefinition.class);
		}
		catch (Exception e) {
			log.error("动态路由【API网关】不存在，错误信息：{}", e.getMessage(), e);
			throw new SystemException(ROUTER_NOT_EXIST);
		}
	}

	/**
	 * 刷新事件.
	 */
	private void publishRefreshRoutesEvent() {
		// 刷新事件
		SpringContextUtils.publishEvent(new RefreshRoutesEvent(this));
	}
	// @formatter:on

}
