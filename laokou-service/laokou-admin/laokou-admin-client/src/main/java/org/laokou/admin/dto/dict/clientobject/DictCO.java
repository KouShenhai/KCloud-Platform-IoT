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

package org.laokou.admin.dto.dict.clientobject;

import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
public class DictCO extends ClientObject {

	private Long id;

	/**
	 * 标签
	 */
	private String label;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private LocalDateTime createDate;

	/**
	 * 排序
	 */
	private Integer sort;

}
