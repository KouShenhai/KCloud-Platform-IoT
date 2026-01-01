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

package org.laokou.admin.loginLog.dto;

import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * 分页查询登录日志命令.
 *
 * @author laokou
 */
@Data
public class LoginLogPageQry extends PageQuery {

	private String username;

	private String type;

	private String ip;

	private String address;

	private String browser;

	private String os;

	private Integer status;

	private String errorMessage;

	public void setUsername(String username) {
		this.username = StringExtUtils.like(StringExtUtils.trim(username));
	}

	public void setIp(String ip) {
		this.ip = StringExtUtils.like(StringExtUtils.trim(ip));
	}

	public void setAddress(String address) {
		this.address = StringExtUtils.like(StringExtUtils.trim(address));
	}

	public void setBrowser(String browser) {
		this.browser = StringExtUtils.like(StringExtUtils.trim(browser));
	}

	public void setOs(String os) {
		this.os = StringExtUtils.like(StringExtUtils.trim(os));
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = StringExtUtils.like(StringExtUtils.trim(errorMessage));
	}

	public void setType(String type) {
		this.type = StringExtUtils.trim(type);
	}

}
