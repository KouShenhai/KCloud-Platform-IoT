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

package org.laokou.admin.dict.api;

import org.laokou.admin.dict.dto.*;
import org.laokou.admin.dict.dto.clientobject.DictCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 字典接口.
 *
 * @author laokou
 */
public interface DictsServiceI {

	/**
	 * 保存字典.
	 * @param cmd 保存命令
	 */
	void save(DictSaveCmd cmd);

	/**
	 * 修改字典.
	 * @param cmd 修改命令
	 */
	void modify(DictModifyCmd cmd);

	/**
	 * 删除字典.
	 * @param cmd 删除命令
	 */
	void remove(DictRemoveCmd cmd);

	/**
	 * 导入字典.
	 * @param cmd 导入命令
	 */
	void importI(DictImportCmd cmd);

	/**
	 * 导出字典.
	 * @param cmd 导出命令
	 */
	void export(DictExportCmd cmd);

	/**
	 * 分页查询字典.
	 * @param qry 分页查询请求
	 */
	Result<Page<DictCO>> page(DictPageQry qry);

	/**
	 * 查看字典.
	 * @param qry 查看请求
	 */
	Result<DictCO> getById(DictGetQry qry);

}
