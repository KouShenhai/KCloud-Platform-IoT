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

package org.laokou.admin.loginLog.api;

import org.laokou.admin.loginLog.dto.*;
import org.laokou.admin.loginLog.dto.clientobject.LoginLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 登录日志接口.
 *
 * @author laokou
 */
public interface LoginLogsServiceI {

	/**
	 * 保存登录日志.
	 * @param cmd 保存命令
	 */
	void save(LoginLogSaveCmd cmd);

	/**
	 * 修改登录日志.
	 * @param cmd 修改命令
	 */
	void modify(LoginLogModifyCmd cmd);

	/**
	 * 删除登录日志.
	 * @param cmd 删除命令
	 */
	void remove(LoginLogRemoveCmd cmd);

	/**
	 * 导入登录日志.
	 * @param cmd 导入命令
	 */
	void importI(LoginLogImportCmd cmd);

	/**
	 * 导出登录日志.
	 * @param cmd 导出命令
	 */
	void export(LoginLogExportCmd cmd);

	/**
	 * 分页查询登录日志.
	 * @param qry 分页查询请求
	 */
	Result<Page<LoginLogCO>> page(LoginLogPageQry qry);

	/**
	 * 查看登录日志.
	 * @param qry 查看请求
	 */
	Result<LoginLogCO> getById(LoginLogGetQry qry);

}
