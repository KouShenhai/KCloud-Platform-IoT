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

package org.laokou.iot.gateway.api;

import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.gateway.dto.GatewayCommandLogPageQry;
import org.laokou.iot.gateway.dto.GatewayReadPropertyCmd;
import org.laokou.iot.gateway.dto.GatewayRebootCmd;
import org.laokou.iot.gateway.dto.GatewayWritePropertyCmd;
import org.laokou.iot.gateway.dto.clientobject.GatewayCommandLogCO;

/**
 *
 * 网关指令接口.
 *
 * @author laokou
 */
public interface GatewayCommandServiceI {

	/**
	 * 重启网关.
	 * @param cmd 重启命令
	 * @return 指令ID
	 */
	Result<Long> reboot(GatewayRebootCmd cmd);

	/**
	 * 读取设备属性.
	 * @param cmd 读取属性命令
	 * @return 指令ID
	 */
	Result<Long> readProperty(GatewayReadPropertyCmd cmd);

	/**
	 * 写入设备属性.
	 * @param cmd 写入属性命令
	 * @return 指令ID
	 */
	Result<Long> writeProperty(GatewayWritePropertyCmd cmd);

	/**
	 * 处理网关指令回执.
	 * @param commandId 指令ID
	 * @param status 指令状态 1成功 2失败
	 * @param result 指令回执内容
	 */
	void handleReply(Long commandId, Integer status, String result);

	/**
	 * 分页查询网关指令日志.
	 * @param qry 分页查询请求
	 */
	Result<Page<GatewayCommandLogCO>> pageCommandLog(GatewayCommandLogPageQry qry);

}
