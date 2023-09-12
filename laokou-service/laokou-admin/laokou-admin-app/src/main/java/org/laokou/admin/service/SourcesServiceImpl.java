/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.SourcesServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.source.*;
import org.laokou.admin.dto.source.clientobject.SourceCO;
import org.laokou.admin.command.source.SourceDeleteCmdExe;
import org.laokou.admin.command.source.SourceInsertCmdExe;
import org.laokou.admin.command.source.SourceUpdateCmdExe;
import org.laokou.admin.command.source.query.SourceGetQryExe;
import org.laokou.admin.command.source.query.SourceListQryExe;
import org.laokou.admin.command.source.query.SourceOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SourcesServiceImpl implements SourcesServiceI {

	private final SourceInsertCmdExe sourceInsertCmdExe;

	private final SourceUpdateCmdExe sourceUpdateCmdExe;

	private final SourceDeleteCmdExe sourceDeleteCmdExe;

	private final SourceListQryExe sourceListQryExe;

	private final SourceGetQryExe sourceGetQryExe;

	private final SourceOptionListQryExe sourceOptionListQryExe;

	@Override
	public Result<Boolean> insert(SourceInsertCmd cmd) {
		return sourceInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> update(SourceUpdateCmd cmd) {
		return sourceUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> deleteById(SourceDeleteCmd cmd) {
		return sourceDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<SourceCO> getById(SourceGetQry qry) {
		return sourceGetQryExe.execute(qry);
	}

	@Override
	public Result<Datas<SourceCO>> list(SourceListQry qry) {
		return sourceListQryExe.execute(qry);
	}

	@Override
	public Result<List<OptionCO>> optionList(SourceOptionListQry qry) {
		return sourceOptionListQryExe.execute(qry);
	}

}
