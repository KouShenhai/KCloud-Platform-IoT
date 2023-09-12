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

package org.laokou.gateway.factory;

import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.laokou.common.i18n.common.Constant;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.gateway.utils.ResponseUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.stereotype.Component;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.SLASH;
import static org.laokou.gateway.exception.ErrorCode.IP_BLACK;

/**
 * RemoteAddrRoutePredicateFactory IP黑名单
 *
 * @author laokou
 */
@Component
public class IpBlackGatewayFilterFactory extends AbstractGatewayFilterFactory<IpBlackGatewayFilterFactory.Config> {

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			if (StringUtil.isEmpty(config.sources)) {
				return chain.filter(exchange);
			}
			List<IpSubnetFilterRule> sources = convert(Arrays.asList(config.sources.split(Constant.COMMA)));
			InetSocketAddress remoteAddress = config.remoteAddressResolver.resolve(exchange);
			for (IpSubnetFilterRule source : sources) {
				if (source.matches(remoteAddress)) {
					return ResponseUtil.response(exchange, Result.fail(IP_BLACK));
				}
			}
			return chain.filter(exchange);
		};
	}

	public IpBlackGatewayFilterFactory() {
		super(Config.class);
	}

	@Data
	static class Config {

		private String sources;

		private volatile RemoteAddressResolver remoteAddressResolver;

		public Config() {
			remoteAddressResolver = new RemoteAddressResolver() {
			};
		}

	}

	private void addSource(List<IpSubnetFilterRule> sources, String source) {
		if (!source.contains(SLASH)) {
			source = source + "/32";
		}
		String[] ipAddressCidrPrefix = source.split(SLASH, 2);
		String ipAddress = ipAddressCidrPrefix[0];
		int cidrPrefix = Integer.parseInt(ipAddressCidrPrefix[1]);
		sources.add(new IpSubnetFilterRule(ipAddress, cidrPrefix, IpFilterRuleType.ACCEPT));
	}

	@NotNull
	private List<IpSubnetFilterRule> convert(List<String> values) {
		List<IpSubnetFilterRule> sources = new ArrayList<>();
		for (String arg : values) {
			addSource(sources, arg);
		}
		return sources;
	}

}