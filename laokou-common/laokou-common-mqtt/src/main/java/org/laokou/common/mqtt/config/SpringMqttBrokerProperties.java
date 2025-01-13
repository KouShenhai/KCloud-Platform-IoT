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

package org.laokou.common.mqtt.config;

import lombok.Data;
import org.laokou.common.core.utils.CollectionUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.mqtt-broker")
public class SpringMqttBrokerProperties implements InitializingBean {

	private Map<String, MqttBrokerProperties> configs = Map.of("default", new MqttBrokerProperties());

	@Override
	public void afterPropertiesSet() {
		configs.forEach((k, v) -> {
			if (CollectionUtil.isEmpty(v.getTopics())) {
				throw new IllegalStateException("Topics must not be empty.");
			}
		});
	}

}
