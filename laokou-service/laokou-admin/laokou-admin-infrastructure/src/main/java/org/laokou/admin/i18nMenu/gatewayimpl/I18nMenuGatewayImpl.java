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

package org.laokou.admin.i18nMenu.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMenu.convertor.I18nMenuConvertor;
import org.laokou.admin.i18nMenu.gateway.I18nMenuGateway;
import org.laokou.admin.i18nMenu.gatewayimpl.database.I18nMenuMapper;
import org.laokou.admin.i18nMenu.gatewayimpl.database.dataobject.I18nMenuDO;
import org.laokou.admin.i18nMenu.model.I18nMenuE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 国际化菜单网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class I18nMenuGatewayImpl implements I18nMenuGateway {

	private final I18nMenuMapper i18nMenuMapper;

	@Override
	public void createI18nMenu(I18nMenuE i18nMenuE) {
		i18nMenuMapper.insert(I18nMenuConvertor.toDataObject(1L, i18nMenuE));
	}

	@Override
	public void updateI18nMenu(I18nMenuE i18nMenuE) {
		I18nMenuDO i18nMenuDO = I18nMenuConvertor.toDataObject(null, i18nMenuE);
		i18nMenuDO.setVersion(i18nMenuMapper.selectVersion(i18nMenuE.getId()));
		i18nMenuMapper.updateById(i18nMenuDO);
	}

	@Override
	public void deleteI18nMenu(Long[] ids) {
		i18nMenuMapper.deleteByIds(Arrays.asList(ids));
	}

}
