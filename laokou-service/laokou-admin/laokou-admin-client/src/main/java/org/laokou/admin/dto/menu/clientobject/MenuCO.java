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

package org.laokou.admin.dto.menu.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.core.utils.TreeUtil;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@Schema(name = "MenuCO", description = "菜单")
public class MenuCO extends TreeUtil.TreeNode<MenuCO> {

	@Serial
	private static final long serialVersionUID = 9057183259302756376L;

	@Schema(name = "icon", description = "菜单图标")
	private String icon;

	@Schema(name = "type", description = "菜单类型 0菜单 1按钮")
	private Integer type;

	@Schema(name = "sort", description = "菜单排序")
	private Integer sort;

	@Schema(name = "component", description = "菜单路由")
	private String component;

	@Schema(name = "permission", description = "菜单权限标识")
	private String permission;

	@Schema(name = "hidden", description = "菜单隐藏 0否 1是")
	private Boolean hidden;

	@Schema(name = "keepAlive", description = "路由缓存 0否 1是")
	private Boolean keepAlive;

	@Schema(name = "status", description = "菜单状态 0正常 1停用")
	private Integer status;

	@Schema(name = "path", description = "菜单路径")
	private String path;

}
