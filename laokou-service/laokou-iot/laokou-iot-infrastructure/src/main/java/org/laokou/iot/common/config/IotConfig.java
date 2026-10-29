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

package org.laokou.iot.common.config;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.BatchReceivePolicy;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.fory.constant.ForyConstants;
import org.laokou.common.mqtt.client.MqttClientManager;
import org.laokou.common.mqtt.client.MqttEventLoop;
import org.laokou.common.mqtt.client.MqttForyFactory;
import org.laokou.iot.device.gatewayimpl.mcp.DeviceMcpMapper;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.annotation.PulsarListenerConsumerBuilderCustomizer;
import org.springframework.pulsar.core.PulsarAdministration;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Configuration(proxyBeanMethods = false)
public class IotConfig {

	static {
		MqttForyFactory.INSTANCE.register(org.laokou.common.mqtt.client.message.UpReportPropertiesGatewayMessage.class,
				ForyConstants.C_244);
		MqttForyFactory.INSTANCE.register(org.laokou.common.mqtt.client.message.UpReportPropertiesDeviceMessage.class,
				ForyConstants.C_245);
	}

	@Bean
	@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.ai.mcp.client",
			name = "enabled")
	DeviceMcpMapper deviceMcpMapper(ToolCallbackProvider syncMcpToolCallbackProvider) {
		return new DeviceMcpMapper(syncMcpToolCallbackProvider);
	}

	@Bean
	@ConditionalOnProperty(havingValue = "false", matchIfMissing = true, prefix = "spring.ai.mcp.client",
			name = "enabled")
	DeviceMcpMapper deviceMcpMapper0() {
		return new DeviceMcpMapper(null);
	}

	@Bean(destroyMethod = "close")
	MqttClientManager mqttClientManager() {
		return new MqttClientManager();
	}

	@Bean(destroyMethod = "close")
	MqttEventLoop mqttEventLoop() {
		return new MqttEventLoop(32);
	}

	/**
	 * 注册原生 PulsarAdmin.
	 * <p>
	 * destroyMethod = "close"： Spring 容器关闭时自动释放 HTTP 连接、线程等资源。
	 */
	@Bean(destroyMethod = "close")
	PulsarAdmin pulsarAdmin(PulsarAdministration pulsarAdministration) throws PulsarClientException {
		return pulsarAdministration.createAdminClient();
	}

	@Bean(initMethod = "createTopic")
	PulsarTopicFactory pulsarTopicFactory(PulsarAdmin pulsarAdmin, SystemSettingsProperties systemSettingsProperties) {
		return new DefaultPulsarTopicFactory(pulsarAdmin, systemSettingsProperties);
	}

	@Bean
	PulsarListenerConsumerBuilderCustomizer<@NonNull String> mqttMessageConsumerCustomizer() {
		return consumer -> consumer
			// 消费者接收队列
			.receiverQueueSize(2000)
			// 一次 batchReceive 最多 1 千条
			.batchReceivePolicy(BatchReceivePolicy.builder()
				.maxNumMessages(1000)
				.maxNumBytes(10 * 1024 * 1024)
				.timeout(3, TimeUnit.SECONDS)
				.build())
			// Shared 方便多个消费者实例水平扩展
			.subscriptionType(SubscriptionType.Shared)
			// ACK 超时时间
			.ackTimeout(10, TimeUnit.SECONDS);
	}

}
