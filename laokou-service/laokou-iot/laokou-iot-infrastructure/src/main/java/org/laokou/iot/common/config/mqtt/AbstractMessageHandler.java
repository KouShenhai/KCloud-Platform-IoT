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

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.mqtt.messages.MqttPublishMessage;
import org.laokou.iot.common.util.VertxMqttUtils;
import org.laokou.iot.session.dto.mqtt.MqttMessageType;

/**
 * @author laokou
 */
public abstract class AbstractMessageHandler implements MessageHandler {

	@Override
	public final boolean supports(String topic) {
		return VertxMqttUtils.matchTopic(getMatchTopic().getTopic(), topic);
	}

	@Override
	public Future<Void> handle(Long snowflakeId, MqttPublishMessage publishMessage) {
		return handleMessage(snowflakeId, publishMessage).onSuccess(_ -> ack(publishMessage));
	}

	protected abstract MqttMessageType getMatchTopic();

	protected abstract Future<Void> handleMessage(Long snowflakeId, MqttPublishMessage publishMessage);

	private void ack(MqttPublishMessage publishMessage) {
		if (publishMessage.qosLevel() == MqttQoS.AT_MOST_ONCE) {
			return;
		}
		publishMessage.ack();
	}

}
