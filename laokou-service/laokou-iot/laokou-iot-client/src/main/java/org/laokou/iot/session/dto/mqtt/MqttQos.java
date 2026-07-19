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

package org.laokou.iot.session.dto.mqtt;

import lombok.Getter;

/***
 * @author laokou
 */
@Getter
enum MqttQos {

	AT_MOST_ONCE(0, "最多一次"),

	AT_LEAST_ONCE(1, "至少一次"),

	EXACTLY_ONCE(2, "恰好一次");

	private final int code;

	private final String desc;

	MqttQos(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
