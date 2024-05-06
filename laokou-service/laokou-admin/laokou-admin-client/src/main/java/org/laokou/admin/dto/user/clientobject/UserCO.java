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

package org.laokou.admin.dto.user.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author laokou
 */
@Data
@Schema(name = "UserCO", description = "用户")
public class UserCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 2478790090537077784L;

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "status", description = "用户状态 0正常 1禁用")
	private Integer status;

	@Schema(name = "roleIds", description = "角色IDS")
	private List<Long> roleIds;

	@Schema(name = "password", description = "密码")
	private String password;

	@Schema(name = "avatar", description = "头像")
	private String avatar;

	@Schema(name = "mail", description = "邮箱")
	private String mail;

	@Schema(name = "mobile", description = "手机号")
	private String mobile;

	@Schema(name = "editor", description = "编辑人")
	private Long editor;

	@Schema(name = "deptId", description = "部门ID")
	private Long deptId;

	@Schema(name = "deptPath", description = "部门PATH")
	private String deptPath;

	@Schema(name = "createDate", description = "创建时间")
	private LocalDateTime createDate;

	@Schema(name = "superAdmin", description = "超级管理员标识 0否 1是")
	private Integer superAdmin;

}
