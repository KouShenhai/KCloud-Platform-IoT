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

package org.laokou.admin.log.notice.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.log.notice.api.NoticeLogsServiceI;
import org.laokou.admin.log.notice.command.NoticeLogExportCmdExe;
import org.laokou.admin.log.notice.command.NoticeLogImportCmdExe;
import org.laokou.admin.log.notice.command.NoticeLogModifyCmdExe;
import org.laokou.admin.log.notice.command.NoticeLogRemoveCmdExe;
import org.laokou.admin.log.notice.command.NoticeLogSaveCmdExe;
import org.laokou.admin.log.notice.command.query.NoticeLogGetQryExe;
import org.laokou.admin.log.notice.command.query.NoticeLogPageQryExe;
import org.laokou.admin.log.notice.dto.NoticeLogExportCmd;
import org.laokou.admin.log.notice.dto.NoticeLogGetQry;
import org.laokou.admin.log.notice.dto.NoticeLogImportCmd;
import org.laokou.admin.log.notice.dto.NoticeLogModifyCmd;
import org.laokou.admin.log.notice.dto.NoticeLogPageQry;
import org.laokou.admin.log.notice.dto.NoticeLogRemoveCmd;
import org.laokou.admin.log.notice.dto.NoticeLogSaveCmd;
import org.laokou.admin.log.notice.dto.clientobject.NoticeLogCO;
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

	private final NoticeLogSaveCmdExe adminNoticeLogSaveCmdExe;

	private final NoticeLogModifyCmdExe noticeLogModifyCmdExe;

	private final NoticeLogRemoveCmdExe noticeLogRemoveCmdExe;

	private final NoticeLogImportCmdExe noticeLogImportCmdExe;

	private final NoticeLogExportCmdExe noticeLogExportCmdExe;

	private final NoticeLogPageQryExe noticeLogPageQryExe;

	private final NoticeLogGetQryExe noticeLogGetQryExe;

	@Override
	public void saveNoticeLog(NoticeLogSaveCmd cmd) {
		adminNoticeLogSaveCmdExe.executeVoid(cmd);
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
