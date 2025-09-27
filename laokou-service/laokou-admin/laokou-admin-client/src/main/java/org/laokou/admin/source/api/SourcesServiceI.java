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

package org.laokou.admin.source.api;

import org.laokou.admin.source.dto.SourceExportCmd;
import org.laokou.admin.source.dto.SourceGetQry;
import org.laokou.admin.source.dto.SourceImportCmd;
import org.laokou.admin.source.dto.SourceModifyCmd;
import org.laokou.admin.source.dto.SourcePageQry;
import org.laokou.admin.source.dto.SourceRemoveCmd;
import org.laokou.admin.source.dto.SourceSaveCmd;
import org.laokou.admin.source.dto.clientobject.SourceCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 数据源接口.
 *
 * @author laokou
 */
public interface SourcesServiceI {

	/**
	 * 保存数据源.
	 * @param cmd 保存命令
	 */
	void saveSource(SourceSaveCmd cmd);

	/**
	 * 修改数据源.
	 * @param cmd 修改命令
	 */
	void modifySource(SourceModifyCmd cmd);

	/**
	 * 删除数据源.
	 * @param cmd 删除命令
	 */
	void removeSource(SourceRemoveCmd cmd);

	/**
	 * 导入数据源.
	 * @param cmd 导入命令
	 */
	void importSource(SourceImportCmd cmd);

	/**
	 * 导出数据源.
	 * @param cmd 导出命令
	 */
	void exportSource(SourceExportCmd cmd);

	/**
	 * 分页查询数据源.
	 * @param qry 分页查询请求
	 */
	Result<Page<SourceCO>> pageSource(SourcePageQry qry);

	/**
	 * 查看数据源.
	 * @param qry 查看请求
	 */
	Result<SourceCO> getSourceById(SourceGetQry qry);

}
