/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.api;

import org.laokou.admin.dto.log.LoginLogExportCmd;
import org.laokou.admin.dto.log.LoginLogListQry;
import org.laokou.admin.dto.log.OperateLogExportCmd;
import org.laokou.admin.dto.log.OperateLogListQry;
import org.laokou.admin.dto.log.clientobject.LoginLogCO;
import org.laokou.admin.dto.log.clientobject.OperateLogCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

/**
 * 日志管理.
 *
 * @author laokou
 */
public interface LogsServiceI {

	/**
	 * 查询操作日志列表.
	 * @param qry 查询操作日志列表参数
	 * @return 操作日志列表
	 */
	Result<Datas<OperateLogCO>> findOperateList(OperateLogListQry qry);

	/**
	 * 导出操作日志.
	 * @param cmd 导出操作日志参数
	 */
	void exportOperate(OperateLogExportCmd cmd);

	/**
	 * 查询登录日志列表.
	 * @param qry 查询登录日志列表参数
	 * @return 登录日志列表
	 */
	Result<Datas<LoginLogCO>> loginList(LoginLogListQry qry);

	/**
	 * 导出登录日志.
	 * @param cmd 导出登录日志参数
	 */
	void exportLogin(LoginLogExportCmd cmd);

}
