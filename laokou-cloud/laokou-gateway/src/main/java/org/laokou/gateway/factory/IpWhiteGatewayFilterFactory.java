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

package org.laokou.gateway.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 仿照 RemoteAddrRoutePredicateFactory IP白名单
 *
 * @author laokou
 */
@Slf4j
@Component
public class IpWhiteGatewayFilterFactory extends AbstractGatewayFilterFactory<Config> {

	private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

	@Override
	public GatewayFilter apply(Config config) {
		reactiveRedisTemplate.opsForHash().put("tenantt3", "333", "n33").subscribe();
		reactiveRedisTemplate.opsForHash().get("tenantt", "ttt").subscribe(r -> log.info("信息,{}", r));
		return Config.apply(config, true);
	}

	public IpWhiteGatewayFilterFactory(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
		super(Config.class);
		this.reactiveRedisTemplate = reactiveRedisTemplate;
	}

}
