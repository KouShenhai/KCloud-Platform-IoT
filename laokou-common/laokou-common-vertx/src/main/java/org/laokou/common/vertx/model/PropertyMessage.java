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

package org.laokou.common.vertx.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.common.constant.StringConstants;

import java.io.Serializable;

/**
 * 属性消息.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
public class PropertyMessage implements Serializable {

	private Long deviceId;

	private Long productId;

	private String payload;

	private String code;

	public PropertyMessage(String topic, String payload, String code) {
		String[] arr = topic.split(StringConstants.SLASH);
		this.productId = Long.valueOf(arr[1]);
		this.deviceId = Long.valueOf(arr[2]);
		this.payload = payload;
		this.code = code;
	}

	public PropertyMessage(Long deviceId, Long productId, String payload, String code) {
		this.deviceId = deviceId;
		this.productId = productId;
		this.payload = payload;
		this.code = code;
	}

}
