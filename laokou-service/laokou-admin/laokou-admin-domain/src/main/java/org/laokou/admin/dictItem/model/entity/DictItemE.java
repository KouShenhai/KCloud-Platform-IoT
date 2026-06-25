/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dictItem.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.annotation.Entity;

import java.io.Serializable;

/**
 * 字典项领域对象【实体】.
 *
 * @author laokou
 */
@Entity
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DictItemE implements Serializable {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 字典项名称.
	 */
	private String name;

	/**
	 * 字典项编码.
	 */
	private String code;

	/**
	 * 字典项排序.
	 */
	private Integer sort;

	/**
	 * 字典项备注.
	 */
	private String remark;

	/**
	 * 字典项状态 0启用 1停用.
	 */
	private Integer status;

	/**
	 * 字典ID.
	 */
	private Long dictId;

}
