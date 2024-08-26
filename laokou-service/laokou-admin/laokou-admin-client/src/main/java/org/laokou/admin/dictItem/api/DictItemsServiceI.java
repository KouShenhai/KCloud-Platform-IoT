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

package org.laokou.admin.dictItem.api;

import org.laokou.admin.dictItem.dto.*;
import org.laokou.admin.dictItem.dto.clientobject.DictItemCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 字典项接口.
 *
 * @author laokou
 */
public interface DictItemsServiceI {

	/**
	 * 保存字典项.
	 * @param cmd 保存命令
	 */
	void save(DictItemSaveCmd cmd);

	/**
	 * 修改字典项.
	 * @param cmd 修改命令
	 */
	void modify(DictItemModifyCmd cmd);

	/**
	 * 删除字典项.
	 * @param cmd 删除命令
	 */
	void remove(DictItemRemoveCmd cmd);

	/**
	 * 导入字典项.
	 * @param cmd 导入命令
	 */
	void importI(DictItemImportCmd cmd);

	/**
	 * 导出字典项.
	 * @param cmd 导出命令
	 */
	void export(DictItemExportCmd cmd);

	/**
	 * 分页查询字典项.
	 * @param qry 分页查询请求
	 */
	Result<Page<DictItemCO>> page(DictItemPageQry qry);

	/**
	 * 查看字典项.
	 * @param qry 查看请求
	 */
	Result<DictItemCO> getById(DictItemGetQry qry);

}
