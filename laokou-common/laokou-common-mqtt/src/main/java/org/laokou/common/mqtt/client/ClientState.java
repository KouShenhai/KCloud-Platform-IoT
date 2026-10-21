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
 * 客户端连接状态机.
 *
 * @author laokou
 */
@Getter
enum ClientState {

	DISCONNECTED(0, "初始/已断开（可发起连接）"),

	CONNECTING(1, "正在建立TCP连接或等待CONNACK"),

	CONNECTED(2, "已连接（收到 CONNACK）"),

	RECONNECTING(3, "连接已丢失，等待重连"),

	CLOSED(4, "用户主动关闭，不再重连");

	private final int code;

	private final String desc;

	ClientState(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
