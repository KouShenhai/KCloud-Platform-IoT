/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.i18nMenu.gatewayimpl.database.I18nMenuMapper;
import org.laokou.admin.i18nMenu.gatewayimpl.database.dataobject.I18nMenuDO;
import org.laokou.admin.i18nMenu.model.I18nMenuA;
import org.laokou.admin.i18nMenu.model.entity.I18nMenuE;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * @author laokou
 */
final class I18nMenuParamValidator {
	private I18nMenuParamValidator() {
	}

	public static ParamValidator.Validate validateId(I18nMenuA i18nMenuA) {
		if (i18nMenuA.isModify() && ObjectUtils.isNull(i18nMenuA.getI18nMenuE().getId())) {
			return ParamValidator.invalidate("国际化菜单ID不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateCode(I18nMenuA i18nMenuA) {
		String code = i18nMenuA.getI18nMenuE().getCode();
		if (StringExtUtils.isEmpty(code)) {
			return ParamValidator.invalidate("国际化菜单编码不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateName(I18nMenuA i18nMenuA) {
		String name = i18nMenuA.getI18nMenuE().getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("国际化菜单名称不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateCodeAndName(I18nMenuA i18nMenuA, I18nMenuMapper i18nMenuMapper) {
		I18nMenuE i18nMenuE = i18nMenuA.getI18nMenuE();
		String code = i18nMenuE.getCode();
		String name = i18nMenuE.getName();
		Long id = i18nMenuE.getId();
		if (StringExtUtils.isNotEmpty(code) && StringExtUtils.isNotEmpty(name)) {
			if (i18nMenuA.isSave() && i18nMenuMapper.selectCount(Wrappers.lambdaQuery(I18nMenuDO.class).eq(I18nMenuDO::getCode, code).eq(I18nMenuDO::getName, name)) > 0) {
				return ParamValidator.invalidate("国际化菜单编码和名称已存在");
			}
			if (i18nMenuA.isModify() && i18nMenuMapper.selectCount(Wrappers.lambdaQuery(I18nMenuDO.class).eq(I18nMenuDO::getCode, code).eq(I18nMenuDO::getName, name).ne(I18nMenuDO::getId, id)) > 0) {
				return ParamValidator.invalidate("国际化菜单编码和名称已存在");
			}
		}
		return ParamValidator.validate();
	}

}
