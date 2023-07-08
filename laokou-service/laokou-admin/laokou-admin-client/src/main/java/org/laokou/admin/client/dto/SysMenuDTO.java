/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class SysMenuDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 3007786510929305220L;

	/**
	 * 类型 0：菜单 1：按钮
	 */
	@NotNull(message = "请选择菜单类型")
	private Integer type;

	/**
	 * 排序
	 */
	@NotNull(message = "菜单排序不为空")
	private Integer sort;

	/**
	 * 菜单路径
	 */
	@NotBlank(message = "菜单路径不为空")
	private String url;

	/**
	 * 权限标识
	 */
	@NotBlank(message = "权限标识不为空")
	private String permission;

	private Long id;

	@NotBlank(message = "菜单名称不为空")
	private String name;

	@NotNull(message = "请选择上级菜单")
	private Long pid;

	@NotBlank(message = "请选择菜单图标")
	private String icon;

	/**
	 * 状态 0显示 1隐藏
	 */
	@NotNull(message = "请选择状态")
	private Integer visible;

}
