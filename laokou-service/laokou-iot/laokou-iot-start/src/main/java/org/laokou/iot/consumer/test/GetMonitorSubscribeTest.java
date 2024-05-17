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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.laokou.common.algorithm.template.Algorithm;
import org.laokou.common.algorithm.template.select.RandomSelectAlgorithm;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.laokou.mqtt.annotation.MqttMessageListener;
import org.laokou.mqtt.config.MqttListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.constants.StringConstant.COMMA;

@Slf4j
@RequiredArgsConstructor
@MqttMessageListener(topic = "/55/D1PGLPG58KZ2/monitor/get")
public class GetMonitorSubscribeTest implements MqttListener {

	// private final MqttTemplate mqttTemplate;

	@Override
	public void onMessage(MqttMessage message) {
		log.info("订阅实时监测消息：{}，已被接收，正在处理中", new String(message.getPayload(), StandardCharsets.UTF_8));
		List<Sensor> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(ResourceUtil.getResource("1.csv").getInputStream()))) {
			String len;
			log.info("读取表头：{}", br.readLine());
			while ((len = br.readLine()) != null) {
				String[] arr = len.split(COMMA);
				list.add(new Sensor(arr[2], arr[3], arr[4]));
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		Algorithm algorithm = new RandomSelectAlgorithm();
		// mqttTemplate.send("/55/D1PGLPG58KZ2/monitor/post", JacksonUtil.toJsonStr(algorithm.select(list, null)));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Sensor implements Serializable {

		private String x;

		private String y;

		private String z;

	}

}
