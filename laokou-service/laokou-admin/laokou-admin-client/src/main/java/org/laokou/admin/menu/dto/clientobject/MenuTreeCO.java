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

package org.laokou.admin.menu.dto.clientobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.laokou.common.core.util.TreeUtils;

import java.time.Instant;

/**
 * 菜单树客户端对象.
 *
 * @author laokou
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuTreeCO extends TreeUtils.TreeNode<MenuTreeCO> {

	/**
	 * 菜单路径.
	 */
	private String path;

	/**
	 * 菜单图标.
	 */
	private String icon;

	/**
	 * 创建时间.
	 */
	private Instant createTime;

	/**
	 * 菜单权限标识.
	 */
	private String permission;

	/**
	 * 菜单类型 0菜单 1按钮.
	 */
	private Integer type;

	/**
	 * 菜单排序.
	 */
	private Integer sort;

	/**
	 * 菜单状态 0启用 1停用.
	 */
	private Integer status;

}
