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

package org.laokou.common.websocket.model.enums;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.EnumParser;
import org.laokou.common.context.util.OAuth2AuthenticatedExtPrincipal;
import org.laokou.common.websocket.config.WebSocketSessionHeartBeatManager;
import org.laokou.common.websocket.config.WebSocketSessionManager;
import org.laokou.common.websocket.model.WebSocketMessage;

/**
 * @author laokou
 */
@Slf4j
@Getter
public enum WebSocketType {

	MESSAGE("message", "消息") {
		@Override
		public void handle(OAuth2AuthenticatedExtPrincipal principal, WebSocketMessage wsm, Channel channel) {
			log.info("【WebSocket-Server】 => 接收到消息，通道ID：{}，用户ID：{}，消息：{}", channel.id().asLongText(), principal.getId(),
					wsm.getPayload());
		}
	},

	CONNECT("connect", "建立连接") {
		@Override
		public void handle(OAuth2AuthenticatedExtPrincipal principal, WebSocketMessage wsm, Channel channel) {
			Long clientId = principal.getId();
			log.info("【WebSocket-Server】 => 已建立连接，通道ID：{}，用户ID：{}", channel.id().asLongText(), clientId);
			WebSocketSessionManager.add(clientId, channel);
		}
	},

	PING("ping", "发送心跳") {
		@Override
		public void handle(OAuth2AuthenticatedExtPrincipal principal, WebSocketMessage wsm, Channel channel) {
		}
	},

	PONG("pong", "心跳应答") {
		@Override
		public void handle(OAuth2AuthenticatedExtPrincipal principal, WebSocketMessage wsm, Channel channel) {
			String channelId = channel.id().asLongText();
			log.info("【WebSocket-Server】 => 接收{}心跳{}", channelId, wsm.getPayload());
			if (WebSocketSessionHeartBeatManager.get(channelId) > 0) {
				WebSocketSessionHeartBeatManager.reset(channelId);
			}
		}
	};

	private final String code;

	private final String desc;

	WebSocketType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static WebSocketType getByCode(String code) {
		return EnumParser.parse(WebSocketType.class, WebSocketType::getCode, code);
	}

	public abstract void handle(OAuth2AuthenticatedExtPrincipal principal, WebSocketMessage wsm, Channel channel);

}
