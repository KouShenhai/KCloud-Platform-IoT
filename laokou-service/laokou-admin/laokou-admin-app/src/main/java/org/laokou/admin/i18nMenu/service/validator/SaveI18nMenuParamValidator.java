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

package org.laokou.admin.i18nMenu.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMenu.gatewayimpl.database.I18nMenuMapper;
import org.laokou.admin.i18nMenu.model.I18nMenuA;
import org.laokou.admin.i18nMenu.model.validator.I18nMenuParamValidator;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component("saveI18nMenuParamValidator")
public class SaveI18nMenuParamValidator implements I18nMenuParamValidator {

	private final I18nMenuMapper i18nMenuMapper;

	@Override
	public void validateI18nMenu(I18nMenuA i18nMenuA) {
		ParamValidator.validate(i18nMenuA.getValidateName(),
				// 校验ID
				org.laokou.admin.i18nMenu.service.validator.I18nMenuParamValidator.validateId(i18nMenuA),
				// 校验编码
				org.laokou.admin.i18nMenu.service.validator.I18nMenuParamValidator.validateCode(i18nMenuA),
				// 校验名称
				org.laokou.admin.i18nMenu.service.validator.I18nMenuParamValidator.validateName(i18nMenuA),
				// 校验编码和名称
				org.laokou.admin.i18nMenu.service.validator.I18nMenuParamValidator.validateCodeAndName(i18nMenuA,
						i18nMenuMapper));
	}

}
