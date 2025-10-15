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

package org.laokou.admin.noticeLog.dto;

import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * 分页查询通知日志命令.
 *
 * @author laokou
 */
@Data
public class NoticeLogPageQry extends PageQuery {

	private String code;

	private String name;

	private Integer status;

	private String errorMessage;

	public void setCode(String code) {
		this.code = StringExtUtils.like(StringExtUtils.trim(code));
	}

	public void setName(String name) {
		this.name = StringExtUtils.like(StringExtUtils.trim(name));
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = StringExtUtils.like(StringExtUtils.trim(errorMessage));
	}

}
