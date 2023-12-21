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

import io.netty.handler.ipfilter.IpSubnetFilterRule;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.Constant;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.ReactiveRedisUtil;
import org.laokou.gateway.utils.RuleUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Data
public class Config {

	private @NotEmpty String sources;

	public static GatewayFilter apply(Config config, boolean white, ReactiveRedisUtil reactiveRedisUtil) {
		try {
			String source = config.getSources();
			List<IpSubnetFilterRule> sources = RuleUtil.convert(Arrays.asList(source.split(Constant.COMMA)));
			return (exchange, chain) -> {
				if (StringUtil.isEmpty(source)) {
					return chain.filter(exchange);
				}
				// InetSocketAddress remoteAddress =
				// config.getRemoteAddressResolver().resolve(exchange);
				// String hostAddress = remoteAddress.getAddress().getHostAddress();
				// if (IpUtil.internalIp(hostAddress)) {
				// return chain.filter(exchange);
				// }
				// if (white && sources.parallelStream().noneMatch(s ->
				// s.matches(remoteAddress))) {
				// local(exchange);
				// log.error("IP为{}被限制", hostAddress);
				// return ResponseUtil.response(exchange, Result.fail(IP_WHITE));
				// }
				// if (!white && sources.parallelStream().anyMatch(s ->
				// s.matches(remoteAddress))) {
				// local(exchange);
				// log.error("IP为{}已列入黑名单", hostAddress);
				// return ResponseUtil.response(exchange, Result.fail(IP_BLACK));
				// }
				return chain.filter(exchange);
			};
		}
		finally {
			LocaleContextHolder.resetLocaleContext();
		}
	}

}
