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

package org.laokou.admin.menu.convertor;

import org.laokou.admin.menu.dto.clientobject.MenuCO;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.menu.model.MenuE;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * 菜单转换器.
 *
 * @author laokou
 */
public class MenuConvertor {

	public static MenuDO toDataObject(MenuE menuE) {
		MenuDO menuDO = ConvertUtil.sourceToTarget(menuE, MenuDO.class);
		if (ObjectUtil.isNull(menuDO.getId())) {
			menuDO.generatorId();
		}
		return menuDO;
	}

	public static MenuCO toClientObject(MenuDO menuDO) {
		return ConvertUtil.sourceToTarget(menuDO, MenuCO.class);
	}

	public static MenuE toEntity(MenuCO menuCO) {
		return ConvertUtil.sourceToTarget(menuCO, MenuE.class);
	}

}
