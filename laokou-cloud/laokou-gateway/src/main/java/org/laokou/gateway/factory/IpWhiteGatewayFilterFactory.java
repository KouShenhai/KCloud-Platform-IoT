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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.Constant;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.LocaleUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.gateway.utils.RequestUtil;
import org.laokou.gateway.utils.ResponseUtil;
import org.laokou.gateway.utils.RuleUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import static org.laokou.common.i18n.common.BizCode.IP_WHITE;

/**
 * 仿照 RemoteAddrRoutePredicateFactory IP白名单
 *
 * @author laokou
 */
@Slf4j
@Component
public class IpWhiteGatewayFilterFactory extends AbstractGatewayFilterFactory<IpWhiteGatewayFilterFactory.Config> {

	@Override
	public GatewayFilter apply(IpWhiteGatewayFilterFactory.Config config) {
		try {
			List<IpSubnetFilterRule> sources = RuleUtil.convert(Arrays.asList(config.sources.split(Constant.COMMA)));
			return (exchange, chain) -> {
				if (StringUtil.isEmpty(config.sources)) {
					return chain.filter(exchange);
				}
				InetSocketAddress remoteAddress = config.remoteAddressResolver.resolve(exchange);
				long count = sources.parallelStream().filter(source -> source.matches(remoteAddress)).count();
				if (count == 0) {
					String language = RequestUtil.getParamValue(exchange.getRequest(), HttpHeaders.ACCEPT_LANGUAGE);
					LocaleContextHolder.setLocale(LocaleUtil.toLocale(language), true);
					log.error("IP为{}被限制", remoteAddress.getAddress().getHostAddress());
					return ResponseUtil.response(exchange, Result.fail(IP_WHITE));
				}
				return chain.filter(exchange);
			};
		}
		finally {
			LocaleContextHolder.resetLocaleContext();
		}
	}

	public IpWhiteGatewayFilterFactory() {
		super(IpWhiteGatewayFilterFactory.Config.class);
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

}
