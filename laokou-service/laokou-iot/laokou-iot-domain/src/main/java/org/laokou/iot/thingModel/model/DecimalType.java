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

import static org.laokou.common.i18n.common.constant.StringConstants.COMMA;
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
				list.add("最大值和最小值不能小于0");
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
			if (!length.contains(COMMA)) {
				list.add("格式不正确，正确格式：整数位数,小数位数");
			}
			else {
				String[] split = length.split(COMMA);
				if (split.length != 2) {
					list.add("格式不正确，正确格式：整数位数,小数位数");
				}
				else {
					if (StringUtils.isEmpty(split[0]) || StringUtils.isEmpty(split[1])) {
						list.add("整数位数和小数位不能为空");
					}
					else {
						if (!RegexUtils.numberRegex(split[0]) || !RegexUtils.numberRegex(split[1])) {
							list.add("整数位数和小数位数只能是正整数");
						}
						else {
							if (Integer.parseInt(split[0]) <= 0) {
								list.add("整数位数不能小于或等于0");
							}
							if (Integer.parseInt(split[1]) <= 0) {
								list.add("小数位数不能小于或等于0");
							}
						}
					}
				}
			}
		}
		return CollectionUtils.isEmpty(list) ? ParamValidator.validate()
				: ParamValidator.invalidate(String.join(DROP, list));
	}

}
