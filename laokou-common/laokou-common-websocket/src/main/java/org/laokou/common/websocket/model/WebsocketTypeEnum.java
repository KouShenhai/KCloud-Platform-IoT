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

package org.laokou.common.websocket.model;

import lombok.Getter;
import org.laokou.common.domain.support.DomainEventPublisher;

import java.nio.channels.Channel;

/**
 * @author laokou
 */
@Getter
public enum WebsocketTypeEnum {

	MESSAGE("message", "消息") {
		@Override
		public void handle(WebsocketMessage websocketMessage, Channel channel,
				DomainEventPublisher domainEventPublisher) {

		}
	},

	CONNECT("connect", "建立连接") {
		@Override
		public void handle(WebsocketMessage websocketMessage, Channel channel,
				DomainEventPublisher domainEventPublisher) {

		}
	},

	PING("ping", "发送心跳") {
		@Override
		public void handle(WebsocketMessage websocketMessage, Channel channel,
				DomainEventPublisher domainEventPublisher) {

		}
	},

	PONG("pong", "心跳应答") {
		@Override
		public void handle(WebsocketMessage websocketMessage, Channel channel,
				DomainEventPublisher domainEventPublisher) {

		}
	};

	private final String code;

	private final String desc;

	WebsocketTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract void handle(WebsocketMessage websocketMessage, Channel channel,
			DomainEventPublisher domainEventPublisher);

}
