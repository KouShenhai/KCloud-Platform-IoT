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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.core.utils.TreeUtil;

/**
 * @author laokou
 */
@Data
@NoArgsConstructor
public class RouterCO extends TreeUtil.TreeNode<RouterCO> {

	private Meta meta;

	private String redirect;

	private String path;

	private Boolean hidden;

	private String component;

	public RouterCO(Long id, Long pid, String name, String title, String redirect, Boolean hidden, String icon,
			Boolean keepAlive, String component, String path) {
		super(id, name, pid);
		this.redirect = redirect;
		this.hidden = hidden;
		this.component = component;
		this.path = path;
		this.meta = new Meta(title, icon, keepAlive);
	}

	@Data
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	static class Meta {

		private String title;

		private String icon;

		private Boolean keepAlive;

	}

}
