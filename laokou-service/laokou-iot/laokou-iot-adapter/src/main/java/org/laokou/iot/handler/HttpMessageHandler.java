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

package org.laokou.iot.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.annotation.PulsarListeners;
import org.springframework.pulsar.listener.AckMode;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
public class HttpMessageHandler {

	@PulsarListeners(value = { @PulsarListener(topicPattern = "persistent://laokouyun/http/up-property-report",
			subscriptionName = "laokouyun-http-up-property-report", schemaType = SchemaType.BYTES, batch = true,
			ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Shared), })
	public void handlePropertyReportMessage(List<byte[]> messages) {
		log.info("接收到HTTP消息：{}", messages);
	}

}
