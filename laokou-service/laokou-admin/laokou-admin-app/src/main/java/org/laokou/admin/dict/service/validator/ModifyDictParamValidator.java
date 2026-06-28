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

package org.laokou.admin.dict.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dict.gatewayimpl.database.DictMapper;
import org.laokou.admin.dict.model.DictA;
import org.laokou.admin.dict.model.validator.DictParamValidator;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component("modifyDictParamValidator")
public class ModifyDictParamValidator implements DictParamValidator {

	private final DictMapper dictMapper;

	@Override
	public void validateDict(DictA dictA) {
		ParamValidator.validate(dictA.getValidateName(),
				// 校验字典ID
				org.laokou.admin.dict.service.validator.DictParamValidator.validateId(dictA),
				// 校验字典编码和字典名称
				org.laokou.admin.dict.service.validator.DictParamValidator.validateCodeAndName(dictA, dictMapper),
				// 校验字典状态
				org.laokou.admin.dict.service.validator.DictParamValidator.validateStatus(dictA));
	}

}
