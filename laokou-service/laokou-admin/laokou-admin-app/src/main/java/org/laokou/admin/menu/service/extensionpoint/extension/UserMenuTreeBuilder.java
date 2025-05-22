/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.menu.service.extensionpoint.extension;

import org.laokou.admin.menu.convertor.MenuConvertor;
import org.laokou.admin.menu.dto.MenuTreeListQry;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.menu.model.MenuStatusEnum;
import org.laokou.admin.menu.model.MenuTypeEnum;
import org.laokou.admin.menu.service.extensionpoint.MenuTreeBuilderExtPt;
import org.laokou.common.core.util.TreeUtils;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.extension.Extension;

import java.util.List;

import static org.laokou.admin.common.constant.BizConstants.*;
import static org.laokou.common.data.cache.constant.NameConstants.USER_MENU;
import static org.laokou.common.data.cache.model.OperateTypeEnum.GET;

/**
 * @author laokou
 */
@Extension(bizId = BIZ_ID_USER, useCase = USE_CASE_MENU, scenario = SCENARIO)
public class UserMenuTreeBuilder implements MenuTreeBuilderExtPt {

	@DataCache(name = USER_MENU, key = "#userId", operateType = GET)
	@Override
	public MenuTreeCO build(MenuTreeListQry qry, Long userId, MenuMapper menuMapper) {
		qry.setStatus(MenuStatusEnum.ENABLE.getCode());
		qry.setType(MenuTypeEnum.MENU.getCode());
		List<MenuDO> list = menuMapper.selectObjectList(qry);
		return TreeUtils.buildTreeNode(MenuConvertor.toClientObjs(list), MenuTreeCO.class);
	}

}
