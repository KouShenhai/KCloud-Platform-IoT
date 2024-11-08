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

package org.laokou.generator.column.api;

import org.laokou.generator.column.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.generator.column.dto.clientobject.ColumnCO;

/**
 *
 * 代码生成器字段接口.
 *
 * @author laokou
 */
public interface ColumnsServiceI {

	/**
	 * 保存代码生成器字段.
	 * @param cmd 保存命令
	 */
	void save(ColumnSaveCmd cmd);

	/**
	 * 修改代码生成器字段.
	 * @param cmd 修改命令
	 */
	void modify(ColumnModifyCmd cmd);

	/**
	 * 删除代码生成器字段.
	 * @param cmd 删除命令
	 */
	void remove(ColumnRemoveCmd cmd);

	/**
	 * 导入代码生成器字段.
	 * @param cmd 导入命令
	 */
	void importI(ColumnImportCmd cmd);

	/**
	 * 导出代码生成器字段.
	 * @param cmd 导出命令
	 */
	void export(ColumnExportCmd cmd);

	/**
	 * 分页查询代码生成器字段.
	 * @param qry 分页查询请求
	 */
	Result<Page<ColumnCO>> page(ColumnPageQry qry);

	/**
	 * 查看代码生成器字段.
	 * @param qry 查看请求
	 */
	Result<ColumnCO> getById(ColumnGetQry qry);

}
