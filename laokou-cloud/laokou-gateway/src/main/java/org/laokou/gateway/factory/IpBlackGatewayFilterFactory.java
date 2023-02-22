/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.gateway.factory;

import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import jakarta.validation.constraints.NotNull;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.gateway.utils.ResponseUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * RemoteAddrRoutePredicateFactory
 * IP黑名单
 * @author laokou
 */
@Component
public class IpBlackGatewayFilterFactory extends AbstractGatewayFilterFactory<IpBlackGatewayFilterFactory.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            List<IpSubnetFilterRule> sources = convert(Arrays.asList(config.sources.split(Constant.COMMA)));
            InetSocketAddress remoteAddress = config.remoteAddressResolver.resolve(exchange);
            for (IpSubnetFilterRule source : sources) {
                if (source.matches(remoteAddress)) {
                    return ResponseUtil.response(exchange,ResponseUtil.error(StatusCode.IP_BLACK));
                }
            }
            return chain.filter(exchange);
        };
    }

    public IpBlackGatewayFilterFactory() {
        super(Config.class);
    }

    static class Config {

        private String sources;

        private RemoteAddressResolver remoteAddressResolver = new RemoteAddressResolver() {};

        public IpBlackGatewayFilterFactory.Config setRemoteAddressResolver(RemoteAddressResolver remoteAddressResolver) {
            this.remoteAddressResolver = remoteAddressResolver;
            return this;
        }

        public String getSources() {
            return sources;
        }

        public void setSources(String sources) {
            this.sources = sources;
        }
    }

    private void addSource(List<IpSubnetFilterRule> sources, String source) {
        if (!source.contains(Constant.FORWARD_SLASH)) {
            source = source + "/32";
        }
        String[] ipAddressCidrPrefix = source.split(Constant.FORWARD_SLASH, 2);
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