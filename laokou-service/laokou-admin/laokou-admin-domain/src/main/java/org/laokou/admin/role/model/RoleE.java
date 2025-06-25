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

package org.laokou.admin.role.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.dto.Identifier;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * 角色领域对象【实体】.
 *
 * @author laokou
 */
@Entity
public class RoleE extends Identifier {

	/**
	 * 角色名称.
	 */
	@Setter
	@Getter
	private String name;

	/**
	 * 角色排序.
	 */
	@Setter
	@Getter
	private Integer sort;

	/**
	 * 数据范围 all全部 custom自定义 dept_self仅本部门 dept部门及以下 self仅本人.
	 */
	@Setter
	@Getter
	private String dataScope;

	/**
	 * 菜单IDS.
	 */
	@Setter
	@Getter
	private List<String> menuIds;

	/**
	 * 部门IDS.
	 */
	@Setter
	@Getter
	private List<String> deptIds;

	/**
	 * 角色菜单IDS.
	 */
	@Setter
	@Getter
	private List<Long> roleMenuIds;

	/**
	 * 角色部门IDS.
	 */
	@Setter
	@Getter
	private List<Long> roleDeptIds;

	/**
	 * 角色IDS.
	 */
	@Setter
	@Getter
	private List<Long> roleIds;

	@Setter
	private RoleOperateTypeEnum roleOperateTypeEnum;

	private final RoleParamValidator saveRoleParamValidator;

	private final RoleParamValidator modifyRoleParamValidator;

	private final RoleParamValidator modifyRoleAuthorityParamValidator;

	public RoleE(@Qualifier("saveRoleParamValidator") RoleParamValidator saveRoleParamValidator,
			@Qualifier("modifyRoleParamValidator") RoleParamValidator modifyRoleParamValidator,
			@Qualifier("modifyRoleAuthorityParamValidator") RoleParamValidator modifyRoleAuthorityParamValidator) {
		this.saveRoleParamValidator = saveRoleParamValidator;
		this.modifyRoleParamValidator = modifyRoleParamValidator;
		this.modifyRoleAuthorityParamValidator = modifyRoleAuthorityParamValidator;
	}

	public void checkRoleParam() {
		switch (roleOperateTypeEnum) {
			case SAVE -> saveRoleParamValidator.validateRole(this);
			case MODIFY -> modifyRoleParamValidator.validateRole(this);
			case MODIFY_AUTHORITY -> modifyRoleAuthorityParamValidator.validateRole(this);
			default -> {
			}
		}
	}

}
