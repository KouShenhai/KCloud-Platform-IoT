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

package org.laokou.admin.dictItem.model;

import lombok.Data;

/**
 * 字典项领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class DictItemE {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 字典标签.
	 */
	private String label;

	/**
	 * 字典值.
	 */
	private String value;

	/**
	 * 字典排序.
	 */
	private Integer sort;

	/**
	 * 字典备注.
	 */
	private String remark;

	/**
	 * 字典状态 0启用 1停用.
	 */
	private Integer status;

	/**
	 * 类型ID.
	 */
	private Long typeId;

}
