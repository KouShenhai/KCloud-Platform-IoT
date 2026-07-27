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

package org.laokou.admin.user.dto.clientobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.fory.annotation.ForyField;
import org.apache.fory.annotation.Nullable;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;
import java.util.List;

/**
 * 用户客户端对象.
 *
 * @author laokou
 */
@Data
@Schema(name = "用户客户端对象", description = "用户客户端对象")
public class UserCO extends ClientObject {

	@ForyField(id = 0)
	@Schema(name = "用户ID", description = "用户ID")
	private Long id;

	@Nullable
	@ForyField(id = 1)
	@Schema(name = "用户密码", description = "用户密码")
	private String password;

	@ForyField(id = 2)
	@Schema(name = "超级管理员标识", description = "超级管理员标识 0否 1是")
	private Integer superAdmin;

	@ForyField(id = 3)
	@Schema(name = "用户邮箱", description = "用户邮箱")
	private String mail;

	@ForyField(id = 4)
	@Schema(name = "用户手机号", description = "用户手机号")
	private String mobile;

	@ForyField(id = 5)
	@Schema(name = "用户状态", description = "用户状态 0启用 1禁用")
	private Integer status;

	@ForyField(id = 6)
	@Schema(name = "用户头像", description = "用户头像")
	private Long avatar;

	@ForyField(id = 7)
	@Schema(name = "用户名", description = "用户名")
	private String username;

	@ForyField(id = 8)
	@Schema(name = "创建时间", description = "创建时间")
	@JsonFormat(pattern = DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS, timezone = DateConstants.DEFAULT_TIMEZONE)
	private Instant createTime;

	@ForyField(id = 9)
	@Schema(name = "角色IDS", description = "角色IDS")
	private List<String> roleIds;

	@ForyField(id = 10)
	@Schema(name = "部门ID", description = "部门ID")
	private Long deptId;

	@ForyField(id = 11)
	@Schema(name = "用户头像URL", description = "用户头像URL")
	private String avatarUrl;

}
