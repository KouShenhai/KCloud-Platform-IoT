/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dto.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;

/**
 * @author laokou
 */
@Data
@Schema(name = "LoginLogListQry", description = "登录日志列表查询参数")
public class LoginLogListQry extends PageQuery {

	@Schema(name = "username", description = "登录的用户名")
	private String username;

	@Schema(name = "status", description = "登录状态 0登录成功 1登录失败")
	private Integer status;

	public void setUsername(String username) {
		this.username = StringUtil.like(username);
	}

}
