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

package org.laokou.mqtt.config;

import io.micrometer.common.lang.NonNullApi;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.mqtt.annotation.MqttMessageListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 可参考 RocketMQ 实现.
 *
 * @author laokou
 */
@Component
@NonNullApi
public final class MqttListenerContainer implements ApplicationListener<ApplicationReadyEvent>, MqttStrategy {

	private final Map<String, MqttListener> MAP = new HashMap<>();

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Map<String, MqttListener> types = SpringContextUtil.getType(MqttListener.class);
		types.values().forEach(item -> {
			boolean annotationPresent = item.getClass().isAnnotationPresent(MqttMessageListener.class);
			if (annotationPresent) {
				String topic = item.getClass().getAnnotation(MqttMessageListener.class).topic();
				MAP.put(topic, item);
			}
		});
	}

	@Override
	public MqttListener get(String topic) {
		return MAP.get(topic);
	}

}
