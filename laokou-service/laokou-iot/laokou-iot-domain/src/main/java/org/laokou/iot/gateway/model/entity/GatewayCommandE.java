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

package org.laokou.iot.gateway.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.annotation.Entity;

import java.io.Serializable;
import java.time.Instant;

/**
 *
 * 网关指令领域对象【实体】.
 *
 * @author laokou
 */
@Entity
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GatewayCommandE implements Serializable {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 会话ID.
	 */
	private Long sessionId;

	/**
	 * 网关ID.
	 */
	private Long gatewayId;

	/**
	 * 网关序列号.
	 */
	private String gatewaySn;

	/**
	 * 指令 reboot_gateway重启网关指令 read_device_properties读取设备属性指令
	 * write_device_properties写入设备属性指令.
	 */
	private String command;

	/**
	 * 主题.
	 */
	private String topic;

	/**
	 * 设备ID.
	 */
	private Long deviceId;

	/**
	 * 设备序列号.
	 */
	private String deviceSn;

	/**
	 * 设备名称.
	 */
	private String deviceName;

	/**
	 * 指令参数，JSON格式.
	 */
	private String param;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

	/**
	 * 指令状态 0待处理 1成功 2失败.
	 */
	private Integer status;

	/**
	 * 指令回复内容，JSON格式.
	 */
	private String replyPayload;

	/**
	 * 指令回复状态 0失败 1成功.
	 */
	private Integer replyStatus;

	/**
	 * 指令下发时间.
	 */
	private Instant downTime;

	/**
	 * 指令回复时间.
	 */
	private Instant replyTime;

	/**
	 * 指令回复信息.
	 */
	private String replyMessage;

}
