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

package org.laokou.iot.gateway.dto;

import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 *
 * 分页查询网关命令.
 *
 * @author laokou
 */
@Data
public class GatewayPageQry extends PageQuery {

	/**
	 * 网关标识.
	 */
	private String gatewayKey;

	/**
	 * 网关名称.
	 */
	private String name;

	/**
	 * 网关状态 0在线 1离线.
	 */
	private Integer status;

	/**
	 * 产品ID.
	 */
	private Long productId;

	public void setGatewayKey(String gatewayKey) {
		this.gatewayKey = StringExtUtils.like(StringExtUtils.trim(gatewayKey));
	}

	public void setName(String name) {
		this.name = StringExtUtils.like(StringExtUtils.trim(name));
	}

}
