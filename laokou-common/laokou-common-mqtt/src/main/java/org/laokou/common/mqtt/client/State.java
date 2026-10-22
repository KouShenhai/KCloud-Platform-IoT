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

package org.laokou.common.mqtt.client;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
enum State {

	WAIT_PUBACK(0, "QoS1：等待 PUBACK"),

	WAIT_PUBREC(1, "QoS2：已发 PUBLISH，等待 PUBREC"),

	WAIT_PUBCOMP(2, "QoS2：已发 PUBREL，等待 PUBCOMP");

	private final int code;

	private final String desc;

	State(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
