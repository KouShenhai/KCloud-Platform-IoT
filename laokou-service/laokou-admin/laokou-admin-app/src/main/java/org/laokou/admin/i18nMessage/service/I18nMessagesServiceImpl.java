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

package org.laokou.admin.i18nMessage.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMessage.api.I18nMessagesServiceI;
import org.laokou.admin.i18nMessage.command.*;
import org.laokou.admin.i18nMessage.command.query.I18nMessageGetQryExe;
import org.laokou.admin.i18nMessage.command.query.I18nMessagePageQryExe;
import org.laokou.admin.i18nMessage.dto.*;
import org.laokou.admin.i18nMessage.dto.clientobject.I18nMessageCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 国际化消息接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class I18nMessagesServiceImpl implements I18nMessagesServiceI {

	private final I18nMessageSaveCmdExe i18nMessageSaveCmdExe;

	private final I18nMessageModifyCmdExe i18nMessageModifyCmdExe;

	private final I18nMessageRemoveCmdExe i18nMessageRemoveCmdExe;

	private final I18nMessageImportCmdExe i18nMessageImportCmdExe;

	private final I18nMessageExportCmdExe i18nMessageExportCmdExe;

	private final I18nMessagePageQryExe i18nMessagePageQryExe;

	private final I18nMessageGetQryExe i18nMessageGetQryExe;

	@Override
	public void save(I18nMessageSaveCmd cmd) {
		i18nMessageSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(I18nMessageModifyCmd cmd) {
		i18nMessageModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(I18nMessageRemoveCmd cmd) {
		i18nMessageRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(I18nMessageImportCmd cmd) {
		i18nMessageImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(I18nMessageExportCmd cmd) {
		i18nMessageExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<I18nMessageCO>> page(I18nMessagePageQry qry) {
		return i18nMessagePageQryExe.execute(qry);
	}

	@Override
	public Result<I18nMessageCO> getById(I18nMessageGetQry qry) {
		return i18nMessageGetQryExe.execute(qry);
	}

}
