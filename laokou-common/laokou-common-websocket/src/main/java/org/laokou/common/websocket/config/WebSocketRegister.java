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

package org.laokou.common.websocket.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author laokou
 */
@Slf4j
public record WebSocketRegister(NacosDiscoveryProperties nacosDiscoveryProperties,
		SpringWebSocketServerProperties springWebSocketServerProperties,
		NamingService namingService) implements ApplicationListener<ApplicationReadyEvent> {

	@Override
	public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
		try {
			namingService.registerInstance(springWebSocketServerProperties.getServiceId(),
					nacosDiscoveryProperties.getGroup(), springWebSocketServerProperties.getIp(),
					springWebSocketServerProperties.getPort(), nacosDiscoveryProperties.getClusterName());
			log.info("【WebSocket-Server】 => 注册Nacos成功");
		}
		catch (NacosException e) {
			log.error("【WebSocket-Server】 => 注册失败：{}", e.getMessage(), e);
		}
	}

}
