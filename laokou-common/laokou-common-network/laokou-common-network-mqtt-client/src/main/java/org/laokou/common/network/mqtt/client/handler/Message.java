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

package org.laokou.common.network.mqtt.client.handler;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.core.util.UUIDGenerator;

import java.io.Serializable;

import static org.laokou.common.i18n.common.constant.StringConstants.SLASH;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
public class Message implements Serializable {

	private Integer msgId;
	private Long deviceId;
	private Long productId;
	private String messageId;
	private String payload;

	public Message(int msgId, String topic, String payload) {
		String[] arr = topic.split(SLASH);
		this.msgId = msgId;
		this.messageId = UUIDGenerator.generateUUID();
		this.deviceId = Long.valueOf(arr[1]);
		this.productId = Long.valueOf(arr[0]);
		this.payload = payload;
	}

}
