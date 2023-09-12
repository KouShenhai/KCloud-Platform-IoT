/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.MenusServiceI;
import org.laokou.admin.dto.menu.*;
import org.laokou.admin.dto.menu.clientobject.MenuCO;
import org.laokou.admin.command.menu.MenuDeleteCmdExe;
import org.laokou.admin.command.menu.MenuInsertCmdExe;
import org.laokou.admin.command.menu.MenuUpdateCmdExe;
import org.laokou.admin.command.menu.query.*;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class MenusServiceImpl implements MenusServiceI {

	private final MenuTreeListQryExe menuTreeListQryExe;

	private final MenuGetQryExe menuGetQryExe;

	private final MenuListQryExe menuListQryExe;

	private final MenuUpdateCmdExe menuUpdateCmdExe;

	private final MenuInsertCmdExe menuInsertCmdExe;

	private final MenuDeleteCmdExe menuDeleteCmdExe;

	private final MenuTreeGetQryExe menuTreeGetQryExe;

	private final MenuIDSGetQryExe menuIDSGetQryExe;

	private final MenuTenantTreeGetQryExe menuTenantTreeGetQryExe;

	@Override
	public Result<MenuCO> treeList(MenuTreeListQry qry) {
		return menuTreeListQryExe.execute(qry);
	}

	@Override
	public Result<List<MenuCO>> list(MenuListQry qry) {
		return menuListQryExe.execute(qry);
	}

	@Override
	public Result<MenuCO> getById(MenuGetQry qry) {
		return menuGetQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> update(MenuUpdateCmd cmd) {
		return menuUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> insert(MenuInsertCmd cmd) {
		return menuInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> deleteById(MenuDeleteCmd cmd) {
		return menuDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<MenuCO> tree(MenuTreeGetQry qry) {
		return menuTreeGetQryExe.execute(qry);
	}

	@Override
	public Result<List<Long>> ids(MenuIDSGetQry qry) {
		return menuIDSGetQryExe.execute(qry);
	}

	@Override
	public Result<MenuCO> tenantTree(MenuTenantTreeGetQry qry) {
		return menuTenantTreeGetQryExe.execute(qry);
	}

}
