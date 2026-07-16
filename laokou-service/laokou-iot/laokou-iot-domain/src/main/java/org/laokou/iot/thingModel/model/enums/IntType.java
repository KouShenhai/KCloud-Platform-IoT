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
public class IntType implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	private String min;

	private String max;

	private String unit;

	private final Integer minVal = -1000000;

	private final Integer maxVal = 1000000;

	public ParamValidator.Validate checkValue() {
		Integer minValue = null;
		Integer maxValue = null;
		if (StringUtils.hasText(min)) {
			minValue = Integer.parseInt(min);
		}
		if (StringUtils.hasText(max)) {
			maxValue = Integer.parseInt(max);
		}
		if (minValue != null && (minValue < minVal || minValue > maxVal)) {
			return ParamValidator.invalidate(String.format("最小值超出范围，最小值必须为%d~%d", minVal, maxVal));
		}
		if (maxValue != null && (maxValue < minVal || maxValue > maxVal)) {
			return ParamValidator.invalidate(String.format("最大值超出范围，最大值必须为%d~%d", minVal, maxVal));
		}
		if (minValue != null && maxValue != null && minValue >= maxValue) {
			return ParamValidator.invalidate("最大值必须大于最小值");
		}
		return ParamValidator.validate();
	}

}
