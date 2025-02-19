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

package org.laokou.generator.column.model;

import lombok.Data;

/**
 *
 * 代码生成器字段领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class ColumnE {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 列名.
	 */
	private String name;

	/**
	 * 描述.
	 */
	private String comment;

	/**
	 * 类型.
	 */
	private String type;

	/**
	 * 必填标识 0否 1是.
	 */
	private Integer requiredFlag;

	/**
	 * 保存标识 0否 1是.
	 */
	private Integer saveFlag;

	/**
	 * 修改标识 0否 1是.
	 */
	private Integer modifyFlag;

	/**
	 * 查询标识 0否 1是.
	 */
	private Integer queryFlag;

	/**
	 * 分页标识 0否 1是.
	 */
	private Integer pageFlag;

	/**
	 * 查询类型.
	 */
	private String queryType;

	/**
	 * 组件类型.
	 */
	private String componentType;

	/**
	 * 字典类型.
	 */
	private String dictType;

	/**
	 * 字段名称.
	 */
	private String fieldName;

	/**
	 * 字段类型.
	 */
	private String fieldType;

	/**
	 * 代码生成信息ID.
	 */
	private Long infoId;

}
