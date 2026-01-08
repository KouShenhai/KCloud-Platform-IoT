/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.websocket.config;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import io.netty.channel.ChannelHandler;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.security.config.OAuth2OpaqueTokenIntrospector;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.context.annotation.Bean;

// @formatter:off
/**
 * @author laokou
 */
public class WebSocketServerConfig {

    @Bean(name = "webSocketServer", initMethod = "start", destroyMethod = "stop")
	public Server webSocketServer(ChannelHandler webSocketServerChannelInitializer, SpringWebSocketServerProperties springWebSocketServerProperties) {
		return new WebSocketServer(webSocketServerChannelInitializer, springWebSocketServerProperties, ThreadUtils.newVirtualTaskExecutor());
    }

	@Bean("webSocketServerChannelInitializer")
	public ChannelHandler webSocketServerChannelInitializer(SpringWebSocketServerProperties springWebSocketServerProperties, ServerProperties serverProperties, ChannelHandler webSocketServerHandler) throws Exception {
		return new WebSocketServerChannelInitializer(springWebSocketServerProperties, webSocketServerHandler, serverProperties);
	}

	@Bean("webSocketServerHandler")
	public ChannelHandler webSocketServerHandler(SpringWebSocketServerProperties springWebSocketServerProperties, OAuth2OpaqueTokenIntrospector oAuth2OpaqueTokenIntrospector) {
		return new WebSocketServerHandler(springWebSocketServerProperties, oAuth2OpaqueTokenIntrospector);
	}

	@Bean
	@ConditionalOnNacosDiscoveryEnabled
	public WebSocketRegister webSocketRegister(NacosDiscoveryProperties nacosDiscoveryProperties, SpringWebSocketServerProperties springWebSocketServerProperties, NacosServiceManager nacosServiceManager) {
		return new WebSocketRegister(nacosDiscoveryProperties, springWebSocketServerProperties, nacosServiceManager.getNamingService());
	}

}
// @formatter:on
