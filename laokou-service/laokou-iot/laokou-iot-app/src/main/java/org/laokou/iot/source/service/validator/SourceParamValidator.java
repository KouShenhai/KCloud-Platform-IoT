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

package org.laokou.iot.source.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.source.model.SourceA;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.laokou.iot.thingModel.model.ThingModelA;
import org.laokou.iot.thingModel.model.entity.ThingModelE;
import org.laokou.iot.thingModel.model.enums.DataType;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author laokou
 */
final class SourceParamValidator {

	private SourceParamValidator() {

	}

	static ParamValidator.Validate validateId(SourceA sourceA) {
		if (sourceA.getId() == null) {
			return ParamValidator.invalidate("数据源ID不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateName(SourceA sourceA) {
		if (!StringUtils.hasText(sourceA.getSourceE().getName())) {
			return ParamValidator.invalidate("数据源名称不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateType(SourceA sourceA) {
		if (!StringUtils.hasText(sourceA.getSourceE().getType())) {
			return ParamValidator.invalidate("数据源类型不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateUsername(SourceA sourceA) {
		if (!StringUtils.hasText(sourceA.getSourceE().getUsername())) {
			return ParamValidator.invalidate("数据源用户名不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validatePassword(SourceA sourceA) {
		if (!StringUtils.hasText(sourceA.getSourceE().getPassword())) {
			return ParamValidator.invalidate("数据源密码不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateEndpoint(SourceA sourceA) {
		if (!StringUtils.hasText(sourceA.getSourceE().getEndpoint())) {
			return ParamValidator.invalidate("数据源地址不能为空");
		}
		return ParamValidator.validate();
	}

}
