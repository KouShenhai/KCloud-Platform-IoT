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

package org.laokou.admin.dto.packages.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.util.List;

/**
 * @author laokou
 */
@Data
@Schema(name = "PackageCO", description = "套餐")
public class PackageCO extends ClientObject {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "name", description = "套餐名称")
	private String name;

	@Schema(name = "menuIds", description = "菜单IDS")
	private List<Long> menuIds;

	public PackageCO(Long id, String name, List<Long> menuIds) {
		this.id = id;
		this.name = name;
		this.menuIds = menuIds;
	}

}
