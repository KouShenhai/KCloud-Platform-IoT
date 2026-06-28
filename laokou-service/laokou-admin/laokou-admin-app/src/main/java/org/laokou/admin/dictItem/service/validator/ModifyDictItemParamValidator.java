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

package org.laokou.admin.dictItem.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dictItem.gatewayimpl.database.DictItemMapper;
import org.laokou.admin.dictItem.model.DictItemA;
import org.laokou.admin.dictItem.model.validator.DictItemParamValidator;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component("modifyDictItemParamValidator")
public class ModifyDictItemParamValidator implements DictItemParamValidator {

	private final DictItemMapper dictItemMapper;

	@Override
	public void validateDict(DictItemA dictItemA) {
		ParamValidator.validate(dictItemA.getValidateName(),
				// 校验字典项ID
				org.laokou.admin.dictItem.service.validator.DictItemParamValidator.validateId(dictItemA),
				// 校验字典项编码和字典项名称
				org.laokou.admin.dictItem.service.validator.DictItemParamValidator.validateCodeAndName(dictItemA,
						dictItemMapper),
				// 校验排序
				org.laokou.admin.dictItem.service.validator.DictItemParamValidator.validateSort(dictItemA),
				// 校验字典项状态
				org.laokou.admin.dictItem.service.validator.DictItemParamValidator.validateStatus(dictItemA));
	}

}
