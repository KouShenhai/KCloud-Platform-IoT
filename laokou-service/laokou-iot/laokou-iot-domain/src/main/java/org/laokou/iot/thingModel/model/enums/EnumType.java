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

package org.laokou.iot.thingModel.model.enums;

import lombok.Data;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@Data
public class EnumType {

	private List<EnumOption> list;

	private Integer length;

	public ParamValidator.Validate checkValue() {
		for (int i = 0; i < list.size(); i++) {
			int index = i + 1;
			String code = list.get(i).getCode();
			String desc = list.get(i).getDesc();
			if (!StringUtils.hasText(code)) {
				return ParamValidator.invalidate(String.format("第%d行编码不能为空", index));
			}
			if (!StringUtils.hasText(desc)) {
				return ParamValidator.invalidate(String.format("第%d行描述不能为空", index));
			}
		}
		List<String> codes = list.stream().map(EnumOption::getCode).toList();
		Set<String> codeSet = new HashSet<>(codes.size());
		for (String code : codes) {
			if (!codeSet.add(code)) {
				return ParamValidator.invalidate(String.format("编码【%s】已存在，请重新输入", code));
			}
		}
		return ParamValidator.validate();
	}

	@Data
	static final class EnumOption {

		private String code;

		private String desc;

	}

}
