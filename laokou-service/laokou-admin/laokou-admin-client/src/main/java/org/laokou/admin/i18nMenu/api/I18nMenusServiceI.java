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

package org.laokou.admin.i18nMenu.api;

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

/**
 * 国际化菜单接口.
 *
 * @author laokou
 */
public interface I18nMenusServiceI {

	/**
	 * 保存国际化菜单.
	 * @param cmd 保存命令
	 */
	void saveI18nMenu(I18nMenuSaveCmd cmd);

	/**
	 * 修改国际化菜单.
	 * @param cmd 修改命令
	 */
	void modifyI18nMenu(I18nMenuModifyCmd cmd);

	/**
	 * 删除国际化菜单.
	 * @param cmd 删除命令
	 */
	void removeI18nMenu(I18nMenuRemoveCmd cmd);

	/**
	 * 导入国际化菜单.
	 * @param cmd 导入命令
	 */
	void importI18nMenu(I18nMenuImportCmd cmd);

	/**
	 * 导出国际化菜单.
	 * @param cmd 导出命令
	 */
	void exportI18nMenu(I18nMenuExportCmd cmd);

	/**
	 * 分页查询国际化菜单.
	 * @param qry 分页查询请求
	 */
	Result<Page<I18nMenuCO>> pageI18nMenu(I18nMenuPageQry qry);

	/**
	 * 查看国际化菜单.
	 * @param qry 查看请求
	 */
	Result<I18nMenuCO> getI18nMenuById(I18nMenuGetQry qry);

}
