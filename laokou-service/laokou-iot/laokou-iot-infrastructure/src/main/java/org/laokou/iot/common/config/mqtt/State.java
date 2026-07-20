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

package org.laokou.iot.common.config.mqtt;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
enum State {
	NEW(0, ""),
	STARTING(0, ""),
	CONNECTING(0, ""),
	SUBSCRIBING(0, ""),
	CONNECTED(0, ""),
	PAUSED(0, ""),
	DISCONNECTED(0, ""),
	RECONNECTING(0, ""),
	CLOSING(0, ""),
	CLOSED(0, "");

	private final int code;
	private final String desc;

	State(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	boolean isOperational() {
		return this == CONNECTED || this == PAUSED;
	}

	boolean isTerminal() {
		return this == CLOSING || this == CLOSED;
	}

}
