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

package org.laokou.admin.noticeLog.api;

import org.laokou.admin.noticeLog.dto.*;
import org.laokou.admin.noticeLog.dto.clientobject.NoticeLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;

/**
 * 通知日志接口.
 *
 * @author laokou
 */
public interface NoticeLogsServiceI {

	/**
	 * 保存通知日志.
	 * @param cmd 保存命令
	 */
	void save(NoticeLogSaveCmd cmd);

	/**
	 * 修改通知日志.
	 * @param cmd 修改命令
	 */
	void modify(NoticeLogModifyCmd cmd);

	/**
	 * 删除通知日志.
	 * @param cmd 删除命令
	 */
	void remove(NoticeLogRemoveCmd cmd);

	/**
	 * 导入通知日志.
	 * @param cmd 导入命令
	 */
	void importI(NoticeLogImportCmd cmd);

	/**
	 * 导出通知日志.
	 * @param cmd 导出命令
	 */
	void export(NoticeLogExportCmd cmd);

	/**
	 * 分页查询通知日志.
	 * @param qry 分页查询请求
	 */
	Result<Page<NoticeLogCO>> page(NoticeLogPageQry qry);

	/**
	 * 查看通知日志.
	 * @param qry 查看请求
	 */
	Result<NoticeLogCO> getById(NoticeLogGetQry qry);

}
