/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.gateway.filter.ip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.nacos.utils.ReactiveResponseUtil;
import org.laokou.common.redis.utils.ReactiveRedisUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static org.laokou.common.i18n.common.exception.SystemException.IP_BLACKED;

/**
 * 黑名单IP.
 *
 * @author laokou
 */
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.cloud.gateway.ip.black",
		name = "enabled")
public class BlackIp extends AbstractIp {

	private final ReactiveRedisUtil reactiveRedisUtil;

	private final RemoteAddressResolver remoteAddressResolver;

	/**
	 * 校验IP并响应（黑名单）.
	 * @param exchange 服务网络交换机
	 * @param chain 链式过滤器
	 * @return 响应结果
	 */
	@Override
	public Mono<Void> validate(ServerWebExchange exchange, GatewayFilterChain chain) {
		InetSocketAddress remoteAddress = remoteAddressResolver.resolve(exchange);
		String hostAddress = remoteAddress.getAddress().getHostAddress();
		if (IpUtil.internalIp(hostAddress)) {
			return chain.filter(exchange);
		}
		String ipCacheHashKey = RedisKeyUtil.getIpCacheHashKey(BLACK);
		return reactiveRedisUtil.hasHashKey(ipCacheHashKey, hostAddress).flatMap(r -> {
			if (Boolean.TRUE.equals(r)) {
				log.error("IP为{}已列入黑名单", hostAddress);
				return ReactiveResponseUtil.response(exchange, Result.fail(IP_BLACKED));
			}
			return chain.filter(exchange);
		});
	}

}
