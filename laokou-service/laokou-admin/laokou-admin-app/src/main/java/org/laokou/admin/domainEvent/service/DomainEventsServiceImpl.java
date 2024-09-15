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

package org.laokou.admin.domainEvent.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domainEvent.api.DomainEventsServiceI;
import org.laokou.admin.domainEvent.command.*;
import org.laokou.admin.domainEvent.command.query.DomainEventGetQryExe;
import org.laokou.admin.domainEvent.command.query.DomainEventPageQryExe;
import org.laokou.admin.domainEvent.dto.*;
import org.laokou.admin.domainEvent.dto.clientobject.DomainEventCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 领域事件接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DomainEventsServiceImpl implements DomainEventsServiceI {

	private final DomainEventSaveCmdExe domainEventSaveCmdExe;

	private final DomainEventModifyCmdExe domainEventModifyCmdExe;

	private final DomainEventRemoveCmdExe domainEventRemoveCmdExe;

	private final DomainEventImportCmdExe domainEventImportCmdExe;

	private final DomainEventExportCmdExe domainEventExportCmdExe;

	private final DomainEventPageQryExe domainEventPageQryExe;

	private final DomainEventGetQryExe domainEventGetQryExe;

	@Override
	public void save(DomainEventSaveCmd cmd) {
		domainEventSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(DomainEventModifyCmd cmd) {
		domainEventModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(DomainEventRemoveCmd cmd) {
		domainEventRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(DomainEventImportCmd cmd) {
		domainEventImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(DomainEventExportCmd cmd) {
		domainEventExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<DomainEventCO>> page(DomainEventPageQry qry) {
		return domainEventPageQryExe.execute(qry);
	}

	@Override
	public Result<DomainEventCO> getById(DomainEventGetQry qry) {
		return domainEventGetQryExe.execute(qry);
	}

}
