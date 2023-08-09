/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.nacos.utils.ConfigUtil;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.laokou.common.nacos.utils.ConfigUtil.ROUTER_DATA_ID;

/**
 * <a href=
 * "https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/src/main/java/com/alibaba/cloud/examples/example/ConfigListenerExample.java">...</a>
 *
 * @author laokou
 */
@Component
@Slf4j
@NonNullApi
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware {

	private final Cache<String, RouteDefinition> caffeineCache;

	private final ConfigUtil configUtil;

	private ApplicationEventPublisher applicationEventPublisher;

	public NacosRouteDefinitionRepository(ConfigUtil configUtil) {
		this.configUtil = configUtil;
		this.caffeineCache = Caffeine.newBuilder().initialCapacity(30).build();
	}

	@PostConstruct
	public void init() throws NacosException {
		log.info("初始化路由配置");
		String group = configUtil.getGroup();
		ConfigService configService = configUtil.getConfigService();
		configService.addListener(ROUTER_DATA_ID, group, new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String configInfo) {
				log.info("收到配置变动通知");
				// 清除缓存
				caffeineCache.invalidateAll();
				// 刷新事件
				applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
			}
		});
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		Collection<RouteDefinition> definitions = caffeineCache.asMap().values();
		if (CollectionUtil.isEmpty(definitions)) {
			try {
				// pull nacos config info
				String group = configUtil.getGroup();
				ConfigService configService = configUtil.getConfigService();
				String configInfo = configService.getConfig(ROUTER_DATA_ID, group, 5000);
				definitions = JacksonUtil.toList(configInfo, RouteDefinition.class);
				return Flux.fromIterable(definitions).doOnNext(route -> caffeineCache.put(route.getId(), route));
			}
			catch (Exception e) {
				return Flux.fromIterable(new ArrayList<>(0));
			}
		}
		return Flux.fromIterable(definitions);
	}

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return Mono.empty();
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return Mono.empty();
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

}
