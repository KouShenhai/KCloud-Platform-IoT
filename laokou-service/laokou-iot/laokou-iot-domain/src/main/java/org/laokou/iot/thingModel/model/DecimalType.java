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

package org.laokou.iot.thingModel.model;

import lombok.Data;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.util.ObjectUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.constant.StringConstants.DROP;

/**
 * @author laokou
 */
@Data
public class DecimalType implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	private Integer integerLength;

	private Integer decimalLength;

	private String unit;

	public ParamValidator.Validate checkValue() {
		List<String> list = new ArrayList<>(2);
		if (ObjectUtils.isEmpty(integerLength) || ObjectUtils.isEmpty(decimalLength)) {
			list.add("整数位长度和小数位长度不能为空");
		}
		else {
			if (integerLength < 1 || integerLength > 64) {
				list.add("整数位长度必须为1-64");
			}
			if (decimalLength < 1 || decimalLength > 4) {
				list.add("小数位长度必须为1-4");
			}
		}
		return CollectionUtils.isEmpty(list) ? ParamValidator.validate()
				: ParamValidator.invalidate(String.join(DROP, list));
	}

}
