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

package org.laokou.admin.menu.model;

import lombok.Getter;
import lombok.Setter;
import org.laokou.admin.user.model.IdGenerator;
import org.laokou.common.i18n.annotation.Entity;
import org.laokou.common.i18n.dto.Identifier;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 菜单领域对象【实体】.
 *
 * @author laokou
 */
@Entity
public class MenuE extends Identifier {

	/**
	 * 菜单父节点ID.
	 */
	@Setter
	@Getter
	private Long pid;

	/**
	 * 菜单权限标识.
	 */
	@Setter
	@Getter
	private String permission;

	/**
	 * 菜单类型 0菜单 1按钮.
	 */
	@Setter
	@Getter
	private Integer type;

	/**
	 * 菜单名称.
	 */
	@Setter
	@Getter
	private String name;

	/**
	 * 菜单路径.
	 */
	@Setter
	@Getter
	private String path;

	/**
	 * 菜单图标.
	 */
	@Setter
	@Getter
	private String icon;

	/**
	 * 菜单排序.
	 */
	@Setter
	@Getter
	private Integer sort;

	/**
	 * 菜单状态 0启用 1停用.
	 */
	@Setter
	@Getter
	private Integer status;

	@Setter
	@Getter
	private MenuOperateTypeEnum menuOperateTypeEnum;

	private final MenuParamValidator saveMenuParamValidator;

	private final MenuParamValidator modifyMenuParamValidator;

	private final IdGenerator idGenerator;

	public MenuE(@Qualifier("saveMenuParamValidator") MenuParamValidator saveMenuParamValidator,
			@Qualifier("modifyMenuParamValidator") MenuParamValidator modifyMenuParamValidator,
			IdGenerator idGenerator) {
		this.saveMenuParamValidator = saveMenuParamValidator;
		this.modifyMenuParamValidator = modifyMenuParamValidator;
		this.idGenerator = idGenerator;
	}

	public Long getPrimaryKey() {
		return idGenerator.getId();
	}

	public void checkMenuParam() {
		switch (menuOperateTypeEnum) {
			case SAVE -> saveMenuParamValidator.validateMenu(this);
			case MODIFY -> modifyMenuParamValidator.validateMenu(this);
			default -> {
			}
		}
	}

}
