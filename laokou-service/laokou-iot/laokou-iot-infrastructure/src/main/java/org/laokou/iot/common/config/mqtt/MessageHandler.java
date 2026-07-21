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

import io.vertx.core.Future;
import io.vertx.mqtt.messages.MqttPublishMessage;

/**
 * 消息处理器.
 *
 * <p>
 * 实现类只负责业务处理，不应主动调用 {@link MqttPublishMessage#ack()}。客户端会在所有匹配的处理器异步成功完成后统一确认消息。
 * </p>
 *
 * @author laokou
 */
public interface MessageHandler {

	/**
	 * 判断是否处理指定主题。默认处理全部主题，基于 {@link AbstractMessageHandler} 的实现会按消息类型自动匹配。
	 * @param topic MQTT主题
	 * @return 是否处理
	 */
	default boolean supports(String topic) {
		return true;
	}

	/**
	 * 异步处理 MQTT 消息.
	 *
	 * <p>
	 * 返回的 Future 必须在业务处理（例如 Pulsar Broker 确认发送）真正完成后才成功；处理失败时应返回失败 Future。客户端只会在 Future
	 * 成功后确认 MQTT 消息。
	 * </p>
	 * @param publishMessage MQTT 发布消息
	 * @return 业务处理完成信号，不能返回 {@code null}
	 */
	Future<Void> handle(MqttPublishMessage publishMessage);

}
