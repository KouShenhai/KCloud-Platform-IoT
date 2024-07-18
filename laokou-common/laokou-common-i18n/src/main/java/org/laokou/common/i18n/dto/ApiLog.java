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

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ApiLog extends DTO {

	private String code;

	private String name;

	private String param;

	private Integer status;

	private String errorMessage;

	private String desc;

	private LocalDateTime timestamp;

	protected abstract String getApiCode();

	protected abstract String getApiName();

	public void update(String param, Integer status, String errorMessage, String desc) {
		this.code = getApiCode();
		this.name = getApiName();
		this.param = param;
		this.status = status;
		this.desc = desc;
		this.errorMessage = errorMessage;
		this.timestamp = DateUtil.now();
	}

}
