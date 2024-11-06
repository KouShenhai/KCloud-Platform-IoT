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

package org.laokou.admin.menu.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.convertor.MenuConvertor;
import org.laokou.admin.menu.gateway.MenuGateway;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.menu.model.MenuE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 菜单网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuGatewayImpl implements MenuGateway {

	private final MenuMapper menuMapper;

	@Override
	public void create(MenuE menuE) {
		menuMapper.insert(MenuConvertor.toDataObject(menuE));
	}

	@Override
	public void update(MenuE menuE) {
		MenuDO menuDO = MenuConvertor.toDataObject(menuE);
		menuDO.setVersion(menuMapper.selectVersion(menuE.getId()));
		menuMapper.updateById(menuDO);
	}

	@Override
	public void delete(Long[] ids) {
		menuMapper.deleteByIds(Arrays.asList(ids));
	}

}
