/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.utils.DateUtil;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ApiLog extends DTO {

	/**
	 * 编码.
	 */
	private String code;

	/**
	 * 名称.
	 */
	private String name;

	/**
	 * 参数.
	 */
	private String param;

	/**
	 * 状态.
	 */
	private Integer status;

	/**
	 * 错误信息.
	 */
	private String errorMessage;

	/**
	 * 备注.
	 */
	private String remark;

	/*
	 * 时间戳.
	 */
	private Instant timestamp;

	protected abstract String getApiCode();

	protected abstract String getApiName();

	public void update(String param, Integer status, String errorMessage, String remark) {
		this.code = getApiCode();
		this.name = getApiName();
		this.param = param;
		this.status = status;
		this.remark = remark;
		this.errorMessage = errorMessage;
		this.timestamp = DateUtil.nowInstant();
	}

}
