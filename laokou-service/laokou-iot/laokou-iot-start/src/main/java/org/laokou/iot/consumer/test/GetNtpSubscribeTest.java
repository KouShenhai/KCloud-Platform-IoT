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

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.mqtt.config.MqttListener;
import org.laokou.common.mqtt.config.MqttMessageExt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetNtpSubscribeTest implements MqttListener {

	@Override
	public void onMessage(MqttMessageExt messageExt) {
		/*
		 * log.info("订阅时钟同步消息：{}，已被接收，正在处理中", new String(message.getPayload(),
		 * StandardCharsets.UTF_8));
		 */
	}

}
