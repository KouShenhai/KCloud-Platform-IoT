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

package org.laokou.admin.ossLog.api;

import org.laokou.admin.ossLog.dto.*;
import org.laokou.admin.ossLog.dto.clientobject.OssLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * OSS日志接口.
 *
 * @author laokou
 */
public interface OssLogsServiceI {

	/**
	 * 保存OSS日志.
	 * @param cmd 保存命令
	 */
	void save(OssLogSaveCmd cmd);

	/**
	 * 修改OSS日志.
	 * @param cmd 修改命令
	 */
	void modify(OssLogModifyCmd cmd);

	/**
	 * 删除OSS日志.
	 * @param cmd 删除命令
	 */
	void remove(OssLogRemoveCmd cmd);

	/**
	 * 导入OSS日志.
	 * @param cmd 导入命令
	 */
	void importI(OssLogImportCmd cmd);

	/**
	 * 导出OSS日志.
	 * @param cmd 导出命令
	 */
	void export(OssLogExportCmd cmd);

	/**
	 * 分页查询OSS日志.
	 * @param qry 分页查询请求
	 */
	Result<Page<OssLogCO>> page(OssLogPageQry qry);

	/**
	 * 查看OSS日志.
	 * @param qry 查看请求
	 */
	Result<OssLogCO> getById(OssLogGetQry qry);

}
