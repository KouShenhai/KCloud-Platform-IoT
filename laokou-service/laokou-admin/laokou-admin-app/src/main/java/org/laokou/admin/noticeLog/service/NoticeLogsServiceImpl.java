/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.noticeLog.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.noticeLog.api.NoticeLogsServiceI;
import org.laokou.admin.noticeLog.command.NoticeLogExportCmdExe;
import org.laokou.admin.noticeLog.command.NoticeLogImportCmdExe;
import org.laokou.admin.noticeLog.command.NoticeLogModifyCmdExe;
import org.laokou.admin.noticeLog.command.NoticeLogRemoveCmdExe;
import org.laokou.admin.noticeLog.command.NoticeLogSaveCmdExe;
import org.laokou.admin.noticeLog.command.query.NoticeLogGetQryExe;
import org.laokou.admin.noticeLog.command.query.NoticeLogPageQryExe;
import org.laokou.admin.noticeLog.dto.NoticeLogExportCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogGetQry;
import org.laokou.admin.noticeLog.dto.NoticeLogImportCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogModifyCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogPageQry;
import org.laokou.admin.noticeLog.dto.NoticeLogRemoveCmd;
import org.laokou.admin.noticeLog.dto.NoticeLogSaveCmd;
import org.laokou.admin.noticeLog.dto.clientobject.NoticeLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 通知日志接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class NoticeLogsServiceImpl implements NoticeLogsServiceI {

	private final NoticeLogSaveCmdExe noticeLogSaveCmdExe;

	private final NoticeLogModifyCmdExe noticeLogModifyCmdExe;

	private final NoticeLogRemoveCmdExe noticeLogRemoveCmdExe;

	private final NoticeLogImportCmdExe noticeLogImportCmdExe;

	private final NoticeLogExportCmdExe noticeLogExportCmdExe;

	private final NoticeLogPageQryExe noticeLogPageQryExe;

	private final NoticeLogGetQryExe noticeLogGetQryExe;

	@Override
	public void saveNoticeLog(NoticeLogSaveCmd cmd) {
		noticeLogSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyNoticeLog(NoticeLogModifyCmd cmd) {
		noticeLogModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeNoticeLog(NoticeLogRemoveCmd cmd) {
		noticeLogRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importNoticeLog(NoticeLogImportCmd cmd) {
		noticeLogImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportNoticeLog(NoticeLogExportCmd cmd) {
		noticeLogExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<NoticeLogCO>> pageNoticeLog(NoticeLogPageQry qry) {
		return noticeLogPageQryExe.execute(qry);
	}

	@Override
	public Result<NoticeLogCO> getNoticeLogById(NoticeLogGetQry qry) {
		return noticeLogGetQryExe.execute(qry);
	}

}
