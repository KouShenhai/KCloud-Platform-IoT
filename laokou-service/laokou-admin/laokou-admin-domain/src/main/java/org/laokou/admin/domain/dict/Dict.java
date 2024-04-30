/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.domain.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AggregateRoot;

/**
 * @author laokou
 */
@Data
@Schema(name = "Dict", description = "字典")
public class Dict extends AggregateRoot<Long> {

	@Schema(name = "label", description = "字典标签")
	private String label;

	@Schema(name = "type", description = "字典类型")
	private String type;

	@Schema(name = "value", description = "字典值")
	private String value;

	@Schema(name = "remark", description = "字典类型")
	private String remark;

	@Schema(name = "sort", description = "字典排序")
	private Integer sort;

	public void checkTypeAndValue(long count) {
		if (count > 0) {
			throw new SystemException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
		}
	}

}
