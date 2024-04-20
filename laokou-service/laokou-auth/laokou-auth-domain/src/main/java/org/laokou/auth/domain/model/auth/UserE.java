/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.auth.domain.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

import static org.laokou.common.i18n.common.NumberConstant.DEFAULT;
import static org.laokou.common.i18n.common.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.SuperAdminEnum.YES;

/**
 * @author laokou
 */
@Data
@Schema(name = "UserE", description = "用户实体")
public class UserE {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "password", description = "密码", example = "123456")
	private String password;

	@Schema(name = "superAdmin", description = "超级管理员标识 0否 1是", example = "1")
	private Integer superAdmin;

	@Schema(name = "avatar", description = "头像", example = "https://pic.cnblogs.com/avatar/simple_avatar.gif")
	private String avatar;

	@Schema(name = "mail", description = "邮箱", example = "2413176044@qq.com")
	private String mail;

	@Schema(name = "status", description = "用户状态 0正常 1锁定", example = "0")
	private Integer status;

	@Schema(name = "mobile", description = "手机号", example = "18974432500")
	private String mobile;

	@Schema(name = "deptId", description = "部门ID")
	private Long deptId;

	@Schema(name = "tenantId", description = "租户ID")
	private Long tenantId;

	@Schema(name = "deptPath", description = "部门PATH")
	private String deptPath;

	public UserE(Long tenantId) {
		this.tenantId = tenantId;
	}

	public UserE(String username, String mail, String mobile) {
		this.username = encrypt(username);
		this.mail = encrypt(mail);
		this.mobile = encrypt(mobile);
	}

	public boolean isSuperAdministrator() {
		return ObjectUtil.equals(YES.ordinal(), this.superAdmin);
	}

	public boolean isDefaultTenant() {
		return ObjectUtil.equals(DEFAULT, this.tenantId);
	}

	private String encrypt(String str) {
		return ObjectUtil.isNotNull(str) ? AesUtil.encrypt(str) : EMPTY;
	}

}
