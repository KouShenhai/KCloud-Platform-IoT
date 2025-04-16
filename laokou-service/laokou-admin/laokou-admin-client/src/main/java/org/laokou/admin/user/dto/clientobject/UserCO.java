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

package org.laokou.admin.user.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;
import java.util.List;

/**
 * 用户客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户客户端对象", description = "用户客户端对象")
public class UserCO extends ClientObject {

	@Schema(name = "用户ID", description = "用户ID")
	private Long id;

	@Schema(name = "用户密码", description = "用户密码")
	private String password;

	@Schema(name = "超级管理员标识", description = "超级管理员标识 0否 1是")
	private Integer superAdmin;

	@Schema(name = "用户邮箱", description = "用户邮箱")
	private String mail;

	@Schema(name = "用户手机号", description = "用户手机号")
	private String mobile;

	@Schema(name = "用户状态", description = "用户状态 0启用 1禁用")
	private Integer status;

	@Schema(name = "用户头像", description = "用户头像")
	private String avatar;

	@Schema(name = "用户名", description = "用户名")
	private String username;

	@Schema(name = "创建时间", description = "创建时间")
	private Instant createTime;

	@Schema(name = "角色IDS", description = "角色IDS")
	private List<String> roleIds;

	@Schema(name = "部门IDS", description = "部门IDS")
	private List<String> deptIds;

}
