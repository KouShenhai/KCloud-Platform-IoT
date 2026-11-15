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

package org.laokou.common.mqtt.client.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 网关指令回复消息【上行】.
 *
 * @author laokou
 */
@Data
public class UpCommandReplyGatewayMessage implements Serializable {

	/**
	 * 租户编码.
	 */
	private String tenantCode;

	/**
	 * 网关SN.
	 */
	private String gatewaySn;

	/**
	 * 时间戳.
	 */
	private Long timestamp;

	/**
	 * 请求ID.
	 */
	private Long requestId;

	/**
	 * 扩展元数据.
	 */
	private Map<String, Object> metadata;

	/**
	 * 设备SN.
	 */
	private String deviceSn;

	/**
	 * 设备名称.
	 */
	private String deviceName;

	/**
	 * 指令.
	 */
	private String command;

	/**
	 * 内容.
	 */
	private Map<String, Object> payload;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

}
