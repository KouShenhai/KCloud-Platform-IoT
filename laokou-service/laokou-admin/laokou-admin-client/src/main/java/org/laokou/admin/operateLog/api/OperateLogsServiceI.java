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

package org.laokou.admin.operateLog.api;

import org.laokou.admin.operateLog.dto.*;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 操作日志接口.
 *
 * @author laokou
 */
public interface OperateLogsServiceI {

	/**
	 * 保存操作日志.
	 * @param cmd 保存命令
	 */
	void save(OperateLogSaveCmd cmd);

	/**
	 * 修改操作日志.
	 * @param cmd 修改命令
	 */
	void modify(OperateLogModifyCmd cmd);

	/**
	 * 删除操作日志.
	 * @param cmd 删除命令
	 */
	void remove(OperateLogRemoveCmd cmd);

	/**
	 * 导入操作日志.
	 * @param cmd 导入命令
	 */
	void importI(OperateLogImportCmd cmd);

	/**
	 * 导出操作日志.
	 * @param cmd 导出命令
	 */
	void export(OperateLogExportCmd cmd);

	/**
	 * 分页查询操作日志.
	 * @param qry 分页查询请求
	 */
	Result<Page<OperateLogCO>> page(OperateLogPageQry qry);

	/**
	 * 查看操作日志.
	 * @param qry 查看请求
	 */
	Result<OperateLogCO> getById(OperateLogGetQry qry);

}
