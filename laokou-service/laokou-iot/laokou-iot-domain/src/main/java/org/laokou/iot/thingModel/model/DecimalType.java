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
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.laokou.common.i18n.common.constant.StringConstants.DROP;

/**
 * @author laokou
 */
@Data
public class DecimalType implements Serializable {

	private BigDecimal min;

	private BigDecimal max;

	private String unit;

	private String length;

	public ParamValidator.Validate checkValue() {
		List<String> list = new ArrayList<>(3);
		if (max == null || min == null) {
			list.add("最大值和最小值不能为空");
		}
		else {
			if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0) {
				list.add("最大值和最小值必须大于0");
			}
			else {
				if (min.compareTo(max) > 0) {
					list.add("最小值不能大于最大值");
				}
			}
		}
		if (StringUtils.isEmpty(length)) {
			list.add("长度不能为空");
		}
		else {
			if (!RegexUtils.matches("([1-9]|[1-5][0-9]|6[0-4]),2?$", length)) {
				list.add("长度格式无效【正确格式：整数位数,小数位数，并且整数位必须大于0，小于100，小数位只能为2】");
			}
		}
		return CollectionUtils.isEmpty(list) ? ParamValidator.validate()
				: ParamValidator.invalidate(String.join(DROP, list));
	}

}
