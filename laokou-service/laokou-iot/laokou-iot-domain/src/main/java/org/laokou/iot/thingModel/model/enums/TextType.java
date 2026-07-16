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

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class TextType implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	private String length;

	private final Integer minVal = 1;

	private final Integer maxVal = 10000;

	public ParamValidator.Validate checkValue() {
		if (!StringUtils.hasText(length)) {
			return ParamValidator.invalidate("长度不能为空");
		}
		int len = Integer.parseInt(length);
		if (len < minVal || len > maxVal) {
			return ParamValidator.invalidate(String.format("长度超出范围，长度必须为%d~%d", minVal, maxVal));
		}
		return ParamValidator.validate();
	}

}
