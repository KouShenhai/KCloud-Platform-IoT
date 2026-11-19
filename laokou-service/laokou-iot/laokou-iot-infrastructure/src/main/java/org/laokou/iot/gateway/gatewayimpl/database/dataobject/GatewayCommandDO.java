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

package org.laokou.iot.gateway.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;
import org.laokou.common.tenant.constant.DSConstants;

import java.time.Instant;

/**
 * 网关指令数据对象.
 *
 * @author laokou
 */
@Data
@TableName(DSConstants.IoT.GATEWAY_COMMAND_TABLE)
public class GatewayCommandDO extends BaseDO {

	/**
	 * 网关ID.
	 */
	private Long gatewayId;

	/**
	 * 网关序列号.
	 */
	private String gatewaySn;

	// @formatter:off
	/**
	 * 下发指令 reboot_gateway重启网关指令 read_device_properties读取设备属性指令 write_device_properties写入设备属性指令.
	 */
	// @formatter:on
	private String command;

	/**
	 * 指令下发主题.
	 */
	private String topic;

	/**
	 * 指令回复主题.
	 */
	private String replyTopic;

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
	 * 指令下发参数.
	 */
	private String param;

	/**
	 * 指令下发信息.
	 */
	private String message;

	/**
	 * 指令下发状态 0成功 1失败.
	 */
	private Integer status;

	/**
	 * 指令回复内容.
	 */
	private String replyPayload;

	/**
	 * 指令回复状态 0成功 1失败.
	 */
	private Integer replyStatus;

	/**
	 * 指令回复时间.
	 */
	private Instant replyTime;

	/**
	 * 指令回复信息.
	 */
	private String replyMessage;

}
