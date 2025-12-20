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

package org.laokou.admin.menu.service.builder;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.convertor.MenuConvertor;
import org.laokou.admin.menu.dto.MenuTreeListQry;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.menu.model.MenuStatusEnum;
import org.laokou.admin.menu.model.MenuTypeEnum;
import org.laokou.common.core.util.TreeUtils;
import org.laokou.common.data.cache.annotation.DistributedCache;
import org.laokou.common.data.cache.constant.NameConstants;
import org.laokou.common.data.cache.model.OperateTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Component("userMenuTreeBuilder")
@RequiredArgsConstructor
public class UserMenuTreeBuilder implements MenuTreeBuilder {

	private final MenuMapper menuMapper;

	@Override
	@DistributedCache(name = NameConstants.USER_MENU, key = "#userId", operateType = OperateTypeEnum.GET)
	public MenuTreeCO buildMenuTree(MenuTreeListQry qry, Long userId) {
		qry.setStatus(MenuStatusEnum.ENABLE.getCode());
		qry.setType(MenuTypeEnum.MENU.getCode());
		List<MenuDO> list = menuMapper.selectObjectList(qry);
		return TreeUtils.buildTreeNode(MenuConvertor.toClientObjs(list), MenuTreeCO.class);
	}

}
