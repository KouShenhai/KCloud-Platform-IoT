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

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.common.i18n.util.SpringContextUtils;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.gateway.constant.GatewayConstants;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.io.Serial;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// @formatter:off
/**
 * nacos动态路由缓存库.
 * <a href="https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/src/main/java/com/alibaba/cloud/examples/example/ConfigListenerExample.java">nacos拉取配置</a>
 *
 * @author laokou
 */
// @formatter:on
@Slf4j
@Repository
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

	static {
		ForyFactory.INSTANCE.register(org.springframework.cloud.gateway.route.RouteDefinition.class);
		ForyFactory.INSTANCE.register(org.springframework.cloud.gateway.filter.FilterDefinition.class);
		ForyFactory.INSTANCE.register(org.springframework.cloud.gateway.handler.predicate.PredicateDefinition.class);
	}

	private final String dataId;

	private final String group;

	private final ConfigService configService;

	private final ReactiveHashOperations<String, String, RouteDefinition> reactiveHashOperations;

	private final ExecutorService virtualThreadExecutor;

	public NacosRouteDefinitionRepository(NacosConfigManager nacosConfigManager,
			ReactiveRedisTemplate<String, Object> reactiveRedisTemplate, ExecutorService virtualThreadExecutor) {
		this.dataId = "router.json";
		this.group = nacosConfigManager.getNacosConfigProperties().getGroup();
		this.configService = nacosConfigManager.getConfigService();
		this.reactiveHashOperations = reactiveRedisTemplate.opsForHash();
		this.virtualThreadExecutor = virtualThreadExecutor;
	}

	@PostConstruct
	public void listenRouter() throws NacosException {
		log.info("开始监听路由配置信息");
		configService.addListener(dataId, group, new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String routes) {
				log.info("监听路由配置信息，开始同步路由配置：{}", routes);
				virtualThreadExecutor.execute(() -> {
					Disposable disposable = syncRouter(getRoutes(routes)).subscribeOn(Schedulers.boundedElastic())
						.subscribe();
					// 发布关闭订阅事件
					SpringContextUtils.publishEvent(new UnsubscribeEvent(this, disposable));
				});
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
	@NonNull
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		return reactiveHashOperations.entries(RedisKeyUtils.getRouteDefinitionHashKey())
			.mapNotNull(Map.Entry::getValue)
			.onErrorContinue((throwable, _) -> {
				if (log.isErrorEnabled()) {
					log.error("从Redis获取路由失败，错误信息：{}", throwable.getMessage(), throwable);
				}
			});
	}
	// @formatter:on

	@NonNull
	@Override
	public Mono<Void> save(@NonNull Mono<RouteDefinition> route) {
		return Mono.empty();
	}

	@NonNull
	@Override
	public Mono<Void> delete(@NonNull Mono<String> routeId) {
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
	private Mono<Void> syncRouter(@NonNull Collection<RouteDefinition> routes) {
		return reactiveHashOperations.delete(RedisKeyUtils.getRouteDefinitionHashKey())
			.doOnError(throwable -> log.error("删除路由失败，错误信息：{}", throwable.getMessage(), throwable))
			.doOnSuccess(_ -> publishRefreshRoutesEvent())
			.thenMany(Flux.fromIterable(routes))
			.flatMap(router -> reactiveHashOperations
				.putIfAbsent(RedisKeyUtils.getRouteDefinitionHashKey(), router.getId(), router)
				.doOnError(throwable -> log.error("保存路由失败，错误信息：{}", throwable.getMessage(), throwable)))
			.then()
			.doOnSuccess(_ -> publishRefreshRoutesEvent());
	}

	// @formatter:off
	/**
	 * 获取nacos动态路由配置.
	 * @return 拉取结果
	 */
	private Collection<RouteDefinition> getRoutes() {
		return getRoutes(StringConstants.EMPTY);
	}

	/**
	 * 获取nacos动态路由配置.
	 * @param str 路由配置
	 * @return 拉取结果
	 */
	private Collection<RouteDefinition> getRoutes(String str) {
		try {
			String routes = StringExtUtils.isEmpty(str) ? configService.getConfig(dataId, group, 5000) : str;
			return JacksonUtils.toList(routes, RouteDefinition.class);
		}
		catch (Exception ex) {
			log.error("动态路由【API网关】不存在，错误信息：{}", ex.getMessage(), ex);
			throw new SystemException(GatewayConstants.ROUTER_NOT_EXIST);
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

	@Getter
	@Setter
	public static class UnsubscribeEvent extends ApplicationEvent {

		@Serial
		private static final long serialVersionUID = 3319752558160144610L;

		private final transient Disposable disposable;

		public UnsubscribeEvent(Object source, Disposable disposable) {
			super(source);
			this.disposable = disposable;
		}

	}

}
