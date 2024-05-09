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

package org.laokou.admin.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;

/**
 * @author laokou
 */
@Data
@Schema(name = "UserListQry", description = "用户列表查询参数")
public class UserListQry extends PageQuery {

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "mobile", description = "手机号")
	private String mobile;

	@Schema(name = "status", description = "用户状态 0正常 1禁用")
	private Integer status;

	@Schema(name = "deptId", description = "部门ID")
	private Long deptId;

	public void setUsername(String username) {
		this.username = StringUtil.like(AesUtil.encrypt(username.trim()));
	}

	public void setMobile(String mobile) {
		this.mobile = StringUtil.like(AesUtil.encrypt(mobile.trim()));
	}

}
