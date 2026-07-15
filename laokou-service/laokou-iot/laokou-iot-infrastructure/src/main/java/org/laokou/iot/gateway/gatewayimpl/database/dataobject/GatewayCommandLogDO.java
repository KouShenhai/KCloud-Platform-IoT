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

/**
 *
 * 网关指令日志数据对象.
 *
 * @author laokou
 */
@Data
@TableName("iot_gateway_command_log")
public class GatewayCommandLogDO extends BaseDO {

	/**
	 * 指令ID.
	 */
	private Long commandId;

	/**
	 * 网关ID.
	 */
	private Long gatewayId;

	/**
	 * 网关标识.
	 */
	private String gatewayKey;

	/**
	 * 指令类型 1重启网关 2读取设备属性 3写入设备属性.
	 */
	private Integer type;

	/**
	 * 设备标识.
	 */
	private String deviceKey;

	/**
	 * 指令内容.
	 */
	private String payload;

	/**
	 * 指令状态 0待处理 1成功 2失败.
	 */
	private Integer status;

	/**
	 * 指令回执.
	 */
	private String result;

}
