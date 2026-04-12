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
import org.laokou.admin.i18nMenu.factory.I18nMenuDomainFactory;
import org.laokou.admin.i18nMenu.gatewayimpl.database.dataobject.I18nMenuDO;
import org.laokou.admin.i18nMenu.model.I18nMenuA;
import org.laokou.admin.i18nMenu.model.entity.I18nMenuE;

import java.util.List;

/**
 * 国际化菜单转换器.
 *
 * @author laokou
 */
public final class I18nMenuConvertor {

	private I18nMenuConvertor() {
	}

	public static I18nMenuDO toDataObject(I18nMenuA i18nMenuA) {
		I18nMenuE i18nMenuE = i18nMenuA.getI18nMenuE();
		I18nMenuDO i18nMenuDO = new I18nMenuDO();
		i18nMenuDO.setId(i18nMenuA.getId());
		i18nMenuDO.setCode(i18nMenuE.getCode());
		i18nMenuDO.setName(i18nMenuE.getName());
		i18nMenuDO.setCreateTime(i18nMenuA.getCreateTime());
		return i18nMenuDO;
	}

	public static I18nMenuCO toClientObject(I18nMenuDO i18nMenuDO) {
		I18nMenuCO i18nMenuCO = new I18nMenuCO();
		i18nMenuCO.setId(i18nMenuDO.getId());
		i18nMenuCO.setCode(i18nMenuDO.getCode());
		i18nMenuCO.setName(i18nMenuDO.getName());
		i18nMenuCO.setCreateTime(i18nMenuDO.getCreateTime());
		return i18nMenuCO;
	}

	public static List<I18nMenuCO> toClientObjectList(List<I18nMenuDO> list) {
		return list.stream().map(I18nMenuConvertor::toClientObject).toList();
	}

	public static I18nMenuE toEntity(I18nMenuCO i18nMenuCO) {
		return I18nMenuDomainFactory.createI18nMenuE()
			.toBuilder()
			.id(i18nMenuCO.getId())
			.code(i18nMenuCO.getCode())
			.name(i18nMenuCO.getName())
			.build();
	}

}
