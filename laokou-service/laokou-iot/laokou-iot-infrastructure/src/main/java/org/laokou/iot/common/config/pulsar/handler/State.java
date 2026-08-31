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

package org.laokou.iot.common.config.pulsar.handler;

import lombok.Getter;

/**
 * @author laokou
 */
@Getter
public enum State {

	INIT("init", "初始化连接"),

	CONNECTING("connecting", "正在建立连接"),

	CONNECTED("connected", "已建立连接"),

	DISCONNECTING("disconnecting", "正在断开连接"),

	DISCONNECTED("disconnected", "已断开连接"),

	RECONNECTING("reconnecting", "正在重新连接");

	private final String code;

	private final String desc;

	State(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
