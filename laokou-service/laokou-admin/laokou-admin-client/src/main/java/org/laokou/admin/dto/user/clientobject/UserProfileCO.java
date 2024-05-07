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

package org.laokou.admin.dto.user.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;
import java.util.Set;

/**
 * @author laokou
 */
@Data
@Schema(name = "UserProfileCO", description = "用户信息")
public class UserProfileCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 5297753219988591611L;

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "avatar", description = "头像")
	private String avatar;

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "tenantId", description = "租户ID")
	private Long tenantId;

	@Schema(name = "permissions", description = "菜单权限标识集合")
	private Set<String> permissions;

	public UserProfileCO(Long id, String avatar, String username, Set<String> permissions, Long tenantId) {
		this.id = id;
		this.avatar = avatar;
		this.username = username;
		this.permissions = permissions;
		this.tenantId = tenantId;
	}

}
