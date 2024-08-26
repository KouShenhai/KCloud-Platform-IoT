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

package org.laokou.admin.menu.model;

import lombok.Data;

/**
 * 菜单领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class MenuE {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 菜单父节点ID.
	 */
	private Long pid;

	/**
	 * 菜单权限标识.
	 */
	private String permission;

	/**
	 * 菜单类型 0菜单 1按钮.
	 */
	private Integer type;

	/**
	 * 菜单名称.
	 */
	private String name;

	/**
	 * 菜单路径.
	 */
	private String path;

	/**
	 * 菜单图标.
	 */
	private String icon;

	/**
	 * 菜单排序.
	 */
	private Integer sort;

	/**
	 * 菜单隐藏 0否 1是.
	 */
	private Integer hidden;

	/**
	 * 菜单状态 0启用 1停用.
	 */
	private Integer status;

	/**
	 * 菜单链接.
	 */
	private String url;

}
