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

package org.laokou.iot.gateway.model.enums;

import lombok.Getter;

/**
 * mq枚举.
 *
 * @author laokou
 */
@Getter
public enum MqTopic {

	DOWN_COMMAND_GATEWAY_MESSAGE("down_command_gateway_message", "下发指令网关消息") {
		@Override
		public String getTopic() {
			return DOWN_COMMAND_GATEWAY_MESSAGE_TOPIC;
		}

		@Override
		public int getNumPartitions() {
			return 2;
		}
	};

	private final String code;

	private final String desc;

	MqTopic(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract String getTopic();

	public abstract int getNumPartitions();

	public static final String DOWN_COMMAND_GATEWAY_MESSAGE_TOPIC = "iot-down-command-gateway-message";

}
