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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.core.utils.TreeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 菜单树客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeCO extends TreeUtil.TreeNode<MenuTreeCO> {

	/**
	 * 菜单路径.
	 */
	private String path;

	/**
	 * 菜单图标.
	 */
	private String icon;

	@JsonIgnore
	private Map<String, Object> extValues = new HashMap<>();

	@JsonIgnore
	private Long id;

	@JsonIgnore
	private Long pid;

}
