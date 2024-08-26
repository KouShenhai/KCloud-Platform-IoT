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

package org.laokou.admin.role.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * 角色客户端对象.
 *
 * @author laokou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCO extends ClientObject {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 角色名称.
	 */
	private String name;

	/**
	 * 角色排序.
	 */
	private Integer sort;

	/**
	 * 数据范围 no无限制 custom自定义 dept_self仅本部门 dept部门及以下 self仅本人.
	 */
	private String dataScope;

}
