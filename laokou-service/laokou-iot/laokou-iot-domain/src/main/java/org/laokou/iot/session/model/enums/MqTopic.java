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

package org.laokou.iot.session.model.enums;

import lombok.Getter;

/**
 * mq枚举.
 *
 * @author laokou
 */
@Getter
public enum MqTopic {

	OPEN_SESSION("open_session", "开启会话") {
		@Override
		public String getTopic() {
			return OPEN_SESSION_MESSAGE_TOPIC;
		}

		@Override
		public int getNumPartitions() {
			return 1;
		}

	},

	CLOSE_SESSION("close_session", "关闭会话") {
		@Override
		public String getTopic() {
			return CLOSE_SESSION_MESSAGE_TOPIC;
		}

		@Override
		public int getNumPartitions() {
			return 1;
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

	public static final String OPEN_SESSION_MESSAGE_TOPIC = "iot-open-session-message";

	public static final String CLOSE_SESSION_MESSAGE_TOPIC = "iot-close-session-message";

}
