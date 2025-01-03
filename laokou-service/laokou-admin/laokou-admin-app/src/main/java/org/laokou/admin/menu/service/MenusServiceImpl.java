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

package org.laokou.admin.menu.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.api.MenusServiceI;
import org.laokou.admin.menu.command.*;
import org.laokou.admin.menu.command.query.MenuGetQryExe;
import org.laokou.admin.menu.command.query.MenuPageQryExe;
import org.laokou.admin.menu.command.query.MenuTreeListQryExe;
import org.laokou.admin.menu.dto.*;
import org.laokou.admin.menu.dto.clientobject.MenuCO;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class MenusServiceImpl implements MenusServiceI {

	private final MenuSaveCmdExe menuSaveCmdExe;

	private final MenuModifyCmdExe menuModifyCmdExe;

	private final MenuRemoveCmdExe menuRemoveCmdExe;

	private final MenuImportCmdExe menuImportCmdExe;

	private final MenuExportCmdExe menuExportCmdExe;

	private final MenuPageQryExe menuPageQryExe;

	private final MenuTreeListQryExe menuTreeListQryExe;

	private final MenuGetQryExe menuGetQryExe;

	@Override
	public void save(MenuSaveCmd cmd) {
		menuSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(MenuModifyCmd cmd) {
		menuModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(MenuRemoveCmd cmd) {
		menuRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(MenuImportCmd cmd) {
		menuImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(MenuExportCmd cmd) {
		menuExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<MenuCO>> page(MenuPageQry qry) {
		return menuPageQryExe.execute(qry);
	}

	@Override
	public Result<List<MenuTreeCO>> treeList(MenuTreeListQry qry) {
		return menuTreeListQryExe.execute(qry);
	}

	@Override
	public Result<MenuCO> getById(MenuGetQry qry) {
		return menuGetQryExe.execute(qry);
	}

}
