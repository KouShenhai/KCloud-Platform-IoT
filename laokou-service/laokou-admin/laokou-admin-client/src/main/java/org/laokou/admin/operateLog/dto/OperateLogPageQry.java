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

package org.laokou.admin.operateLog.dto;

import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;

import static org.laokou.common.i18n.utils.StringUtil.trim;

/**
 * 分页查询操作日志命令.
 *
 * @author laokou
 */
@Data
public class OperateLogPageQry extends PageQuery {

	private String name;

	private String moduleName;

	private String errorMessage;

	private String ip;

	private String requestType;

	private String operator;

	private String profile;

	private Integer status;

	public void setOperator(String operator) {
		this.operator = StringUtil.like(trim(operator));
	}

	public void setName(String name) {
		this.name = StringUtil.like(trim(name));
	}

	public void setModuleName(String moduleName) {
		this.moduleName = StringUtil.like(trim(moduleName));
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = StringUtil.like(trim(errorMessage));
	}

	public void setIp(String ip) {
		this.ip = StringUtil.like(trim(ip));
	}

	public void setRequestType(String requestType) {
		this.requestType = StringUtil.like(trim(requestType));
	}

	public void setProfile(String profile) {
		this.profile = StringUtil.like(trim(profile));
	}

}
