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

package org.laokou.network.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pulsar.common.schema.SchemaType;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.springframework.pulsar.annotation.PulsarMessage;

import java.io.Serializable;

/**
 * 属性消息.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@PulsarMessage(schemaType = SchemaType.BYTES)
public class PropertyMessage implements Serializable {

	private Long productId;

	private Long deviceId;

	private String code;

	private String payload;

	public PropertyMessage(String topic, String code, String payload) {
		String[] arr = topic.split(StringConstants.SLASH);
		this.productId = Long.valueOf(arr[1]);
		this.deviceId = Long.valueOf(arr[2]);
		this.code = code;
		this.payload = payload;
	}

}
