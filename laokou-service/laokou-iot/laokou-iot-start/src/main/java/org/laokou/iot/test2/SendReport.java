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

package org.laokou.iot.test2;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.laokou.mqtt.template.MqttTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendReport {

	// private final MqttTemplate mqttTemplate;

	@PostConstruct
	public void test() {
		String s = """
				{
					 "timestamp":1601196762389,
			   "messageId":"1",
			   "deviceId":"1795662141836713984",
			   "headers":{
			     "productId":"1795661364212752384",
			     "deviceName":"设备测试",
			   	"keepOnlineTimeoutSeconds":30,
			     "keepOnline":true
			   },
					"properties":{"temperature":36.8}
				}
				""";
		 //mqttTemplate.send("/1795661364212752384/1795662141836713984/properties/report",
		 // s);
	}

}
