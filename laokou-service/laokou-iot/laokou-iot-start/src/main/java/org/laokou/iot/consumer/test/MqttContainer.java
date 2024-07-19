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

package org.laokou.iot.consumer.test;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.laokou.common.mqtt.config.MqttLoadBalancer;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
public class MqttContainer implements MqttLoadBalancer {

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		System.out.println(topic);
	}

}
