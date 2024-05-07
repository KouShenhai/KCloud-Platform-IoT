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

package org.laokou.admin.domain.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AggregateRoot;

/**
 * @author laokou
 */
@Data
@Schema(name = "Menu", description = "菜单")
public class Menu extends AggregateRoot<Long> {

	@Schema(name = "name", description = "菜单名称")
	private String name;

	@Schema(name = "pid", description = "菜单父节点ID")
	private Long pid;

	@Schema(name = "icon", description = "菜单图标")
	private String icon;

	@Schema(name = "type", description = "菜单类型 0菜单 1按钮")
	private Integer type;

	@Schema(name = "sort", description = "菜单排序")
	private Integer sort;

	@Schema(name = "url", description = "菜单URL")
	private String url;

	@Schema(name = "permission", description = "菜单权限标识")
	private String permission;

	@Schema(name = "visible", description = "菜单状态 0显示 1隐藏")
	private Integer visible;

	public void checkName(long count) {
		if (count > 0) {
			throw new SystemException("菜单名称已存在，请重新填写");
		}
	}

}
