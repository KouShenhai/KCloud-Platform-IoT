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

package org.laokou.admin.menu.api;

import org.laokou.admin.menu.dto.MenuExportCmd;
import org.laokou.admin.menu.dto.MenuGetQry;
import org.laokou.admin.menu.dto.MenuImportCmd;
import org.laokou.admin.menu.dto.MenuModifyCmd;
import org.laokou.admin.menu.dto.MenuPageQry;
import org.laokou.admin.menu.dto.MenuRemoveCmd;
import org.laokou.admin.menu.dto.MenuSaveCmd;
import org.laokou.admin.menu.dto.MenuTreeListQry;
import org.laokou.admin.menu.dto.clientobject.MenuCO;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 菜单接口.
 *
 * @author laokou
 */
public interface MenusServiceI {

	/**
	 * 保存菜单.
	 * @param cmd 保存命令
	 */
	void saveMenu(MenuSaveCmd cmd);

	/**
	 * 修改菜单.
	 * @param cmd 修改命令
	 */
	void modifyMenu(MenuModifyCmd cmd);

	/**
	 * 删除菜单.
	 * @param cmd 删除命令
	 */
	void removeMenu(MenuRemoveCmd cmd);

	/**
	 * 导入菜单.
	 * @param cmd 导入命令
	 */
	void importMenu(MenuImportCmd cmd);

	/**
	 * 导出菜单.
	 * @param cmd 导出命令
	 */
	void exportMenu(MenuExportCmd cmd);

	/**
	 * 分页查询菜单列表.
	 * @param qry 分页查询请求
	 */
	Result<Page<MenuCO>> pageMenu(MenuPageQry qry);

	/**
	 * 查询菜单树列表.
	 * @param qry 查询请求
	 */
	Result<List<MenuTreeCO>> listTreeMenu(MenuTreeListQry qry);

	/**
	 * 查看菜单.
	 * @param qry 查看请求
	 */
	Result<MenuCO> getMenuById(MenuGetQry qry);

}
