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
import org.laokou.common.fory.constant.ForyConstants;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 上报设备属性消息【上行】.
 *
 * @author laokou
 */
@Data
public class UpReportPropertiesDeviceMessage implements Serializable {

	@Serial
	private static final long serialVersionUID = ForyConstants.C_245;

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
	 * 设备属性.
	 */
	private Map<String, Object> properties;

}
