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
import org.laokou.common.i18n.util.ParamValidator;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class StringType implements Serializable {

	@Serial
	private static final long serialVersionUID = -1L;

	private Integer length;

	public ParamValidator.Validate checkValue() {
		if (length == null) {
			return ParamValidator.invalidate("长度不能为空");
		}
		if (length < 1 || length > 2000) {
			return ParamValidator.invalidate("长度必须为1-2000");
		}
		return ParamValidator.validate();
	}

}
