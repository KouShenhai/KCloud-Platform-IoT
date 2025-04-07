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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.constant.StringConstants.DROP;
import static org.laokou.common.i18n.util.ParamValidator.invalidate;

/**
 * @author laokou
 */
@Data
public class IntegerType implements Serializable {

	private Integer min;

	private Integer max;

	private String unit;

	private Integer length;

	public ParamValidator.Validate checkValue() {
		List<String> list = new ArrayList<>(3);
		if (min == null) {
			list.add("最小值不能为空");
		}
		if (max == null) {
			list.add("最大值不能为空");
		}
		if (max != null && min != null && min > max) {
			list.add("最小值不能大于最大值");
		}
		if (length == null) {
			list.add("长度不能为空");
		}
		return CollectionUtils.isEmpty(list) ? ParamValidator.validate() : invalidate(String.join(DROP, list));
	}

}
