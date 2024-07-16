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

package org.laokou.common.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

	private GYY gyy = new GYY();

	@Data
	public static class GYY {

		private boolean enabled = true;

		private String templateId = "ENC(YpExkbRJjCJUNB9QWmqzSmJl9XJ2BMnw2KmxxQ5JPXbhB66Z9ARRPJ3o4cuZNcxDgaLsLB0Qnplvvagdn0XJN7iZOPSL7P8pg3iFIoS8RKE=)";

		private String signId = "ENC(anORfPoXwjam7f2azlNZAJbPfgmYUd6p8OVOyyNnTNgHNZPesJhYXE9mvqvpgHFSit3aNeIyBuJS6j1PSsFs6m0aJdL0NjvKNwPd2aGTLog=)";

		private String appcode = "ENC(Mlk2DvR7RwOjEj0EajZ+9cjHqHK9Qzkmti7U1r8/iVDj2bFOwoO1ZdwbaYon/1QivDL1hu4vvxFVNv56mVdqCQiPQrhPW3oIJRDd9sQBfWA=)";

	}

}
