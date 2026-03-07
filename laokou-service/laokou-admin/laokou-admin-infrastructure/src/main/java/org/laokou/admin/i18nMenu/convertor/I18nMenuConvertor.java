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

package org.laokou.admin.i18nMenu.convertor;

import org.laokou.admin.i18nMenu.dto.clientobject.I18nMenuCO;
import org.laokou.admin.i18nMenu.gatewayimpl.database.dataobject.I18nMenuDO;
import org.laokou.admin.i18nMenu.model.I18nMenuE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.i18n.util.ObjectUtils;

/**
 * 国际化菜单转换器.
 *
 * @author laokou
 */
public final class I18nMenuConvertor {

	private I18nMenuConvertor() {
	}

	public static I18nMenuDO toDataObject(Long id, I18nMenuE i18nMenuE) {
		I18nMenuDO i18nMenuDO = ConvertUtils.sourceToTarget(i18nMenuE, I18nMenuDO.class);
		if (ObjectUtils.isNull(i18nMenuDO.getId())) {
			i18nMenuDO.setId(id);
		}
		return i18nMenuDO;
	}

	public static I18nMenuCO toClientObject(I18nMenuDO i18nMenuDO) {
		return ConvertUtils.sourceToTarget(i18nMenuDO, I18nMenuCO.class);
	}

	public static I18nMenuE toEntity(I18nMenuCO i18nMenuCO) {
		return ConvertUtils.sourceToTarget(i18nMenuCO, I18nMenuE.class);
	}

}
