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

package org.laokou.admin.dictType.api;

import org.laokou.admin.dictType.dto.*;
import org.laokou.admin.dictType.dto.clientobject.DictTypeCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 字典类型接口.
 *
 * @author laokou
 */
public interface DictTypesServiceI {

	/**
	 * 保存字典类型.
	 * @param cmd 保存命令
	 */
	void save(DictTypeSaveCmd cmd);

	/**
	 * 修改字典类型.
	 * @param cmd 修改命令
	 */
	void modify(DictTypeModifyCmd cmd);

	/**
	 * 删除字典类型.
	 * @param cmd 删除命令
	 */
	void remove(DictTypeRemoveCmd cmd);

	/**
	 * 导入字典类型.
	 * @param cmd 导入命令
	 */
	void importI(DictTypeImportCmd cmd);

	/**
	 * 导出字典类型.
	 * @param cmd 导出命令
	 */
	void export(DictTypeExportCmd cmd);

	/**
	 * 分页查询字典类型.
	 * @param qry 分页查询请求
	 */
	Result<Page<DictTypeCO>> page(DictTypePageQry qry);

	/**
	 * 查看字典类型.
	 * @param qry 查看请求
	 */
	Result<DictTypeCO> getById(DictTypeGetQry qry);

}
