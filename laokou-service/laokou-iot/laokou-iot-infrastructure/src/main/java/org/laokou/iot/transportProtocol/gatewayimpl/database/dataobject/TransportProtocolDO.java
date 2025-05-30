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

package org.laokou.iot.transportProtocol.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.mapper.BaseDO;

/**
 *
 * 传输协议数据对象.
 *
 * @author laokou
 */
@Data
@TableName("iot_transport_protocol")
public class TransportProtocolDO extends BaseDO {

	/**
	 * 协议名称.
	 */
	private String name;

	/**
	 * 协议类型.
	 */
	private String type;

	/**
	 * 主机.
	 */
	private String host;

	/**
	 * 端口.
	 */
	private String port;

	/**
	 * 客户端ID.
	 */
	private String clientId;

	/**
	 * 用户名.
	 */
	private String username;

	/**
	 * 密码.
	 */
	private String password;

	/**
	 * 备注.
	 */
	private String remark;

}
