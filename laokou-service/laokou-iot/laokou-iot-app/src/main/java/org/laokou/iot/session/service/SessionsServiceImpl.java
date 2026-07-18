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

package org.laokou.iot.session.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.session.api.SessionsServiceI;
import org.laokou.iot.session.command.SessionModifyCmdExe;
import org.laokou.iot.session.command.SessionRemoveCmdExe;
import org.laokou.iot.session.command.SessionSaveCmdExe;
import org.laokou.iot.session.command.query.SessionGetQryExe;
import org.laokou.iot.session.command.query.SessionPageQryExe;
import org.laokou.iot.session.dto.SessionGetQry;
import org.laokou.iot.session.dto.SessionModifyCmd;
import org.laokou.iot.session.dto.SessionPageQry;
import org.laokou.iot.session.dto.SessionRemoveCmd;
import org.laokou.iot.session.dto.SessionSaveCmd;
import org.laokou.iot.session.dto.clientobject.SessionCO;
import org.springframework.stereotype.Service;

/**
 * 会话接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SessionsServiceImpl implements SessionsServiceI {

	private final SessionSaveCmdExe sessionSaveCmdExe;

	private final SessionModifyCmdExe sessionModifyCmdExe;

	private final SessionRemoveCmdExe sessionRemoveCmdExe;

	private final SessionPageQryExe sessionPageQryExe;

	private final SessionGetQryExe sessionGetQryExe;


	@Override
	public void saveSession(SessionSaveCmd cmd) {

	}

	@Override
	public void modifySession(SessionModifyCmd cmd) {

	}

	@Override
	public void removeSession(SessionRemoveCmd cmd) {

	}

	@Override
	public Result<Page<SessionCO>> pageSession(SessionPageQry qry) {
		return null;
	}

	@Override
	public Result<SessionCO> getSessionById(SessionGetQry qry) {
		return sessionGetQryExe.execute(qry);
	}
}
