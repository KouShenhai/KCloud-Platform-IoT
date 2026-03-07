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

package org.laokou.admin.i18nMenu.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMenu.api.I18nMenusServiceI;
import org.laokou.admin.i18nMenu.command.I18nMenuExportCmdExe;
import org.laokou.admin.i18nMenu.command.I18nMenuImportCmdExe;
import org.laokou.admin.i18nMenu.command.I18nMenuModifyCmdExe;
import org.laokou.admin.i18nMenu.command.I18nMenuRemoveCmdExe;
import org.laokou.admin.i18nMenu.command.I18nMenuSaveCmdExe;
import org.laokou.admin.i18nMenu.command.query.I18nMenuGetQryExe;
import org.laokou.admin.i18nMenu.command.query.I18nMenuPageQryExe;
import org.laokou.admin.i18nMenu.dto.I18nMenuExportCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuGetQry;
import org.laokou.admin.i18nMenu.dto.I18nMenuImportCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuModifyCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuPageQry;
import org.laokou.admin.i18nMenu.dto.I18nMenuRemoveCmd;
import org.laokou.admin.i18nMenu.dto.I18nMenuSaveCmd;
import org.laokou.admin.i18nMenu.dto.clientobject.I18nMenuCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 国际化菜单接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class I18nMenusServiceImpl implements I18nMenusServiceI {

	private final I18nMenuSaveCmdExe i18nMenuSaveCmdExe;

	private final I18nMenuModifyCmdExe i18nMenuModifyCmdExe;

	private final I18nMenuRemoveCmdExe i18nMenuRemoveCmdExe;

	private final I18nMenuImportCmdExe i18nMenuImportCmdExe;

	private final I18nMenuExportCmdExe i18nMenuExportCmdExe;

	private final I18nMenuPageQryExe i18nMenuPageQryExe;

	private final I18nMenuGetQryExe i18nMenuGetQryExe;

	@Override
	public void saveI18nMenu(I18nMenuSaveCmd cmd) {
		i18nMenuSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyI18nMenu(I18nMenuModifyCmd cmd) {
		i18nMenuModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeI18nMenu(I18nMenuRemoveCmd cmd) {
		i18nMenuRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI18nMenu(I18nMenuImportCmd cmd) {
		i18nMenuImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportI18nMenu(I18nMenuExportCmd cmd) {
		i18nMenuExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<I18nMenuCO>> pageI18nMenu(I18nMenuPageQry qry) {
		return i18nMenuPageQryExe.execute(qry);
	}

	@Override
	public Result<I18nMenuCO> getI18nMenuById(I18nMenuGetQry qry) {
		return i18nMenuGetQryExe.execute(qry);
	}

}
