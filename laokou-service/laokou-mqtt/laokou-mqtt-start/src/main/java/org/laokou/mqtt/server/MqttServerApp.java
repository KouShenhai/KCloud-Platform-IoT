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

package org.laokou.mqtt.server;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "org.laokou")
public class MqttServerApp {

	public static void main(String[] args) {
		// 启用虚拟线程支持
		System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");
		new SpringApplicationBuilder(MqttServerApp.class).web(WebApplicationType.REACTIVE).run(args);
	}

}
