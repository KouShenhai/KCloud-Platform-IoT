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
import org.laokou.admin.api.DictsServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.dict.*;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.command.dict.DictDeleteCmdExe;
import org.laokou.admin.command.dict.DictInsertCmdExe;
import org.laokou.admin.command.dict.DictUpdateCmdExe;
import org.laokou.admin.command.dict.query.DictGetQryExe;
import org.laokou.admin.command.dict.query.DictListQryExe;
import org.laokou.admin.command.dict.query.DictOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsServiceI {

	private final DictInsertCmdExe dictInsertCmdExe;

	private final DictUpdateCmdExe dictUpdateCmdExe;

	private final DictDeleteCmdExe dictDeleteCmdExe;

	private final DictOptionListQryExe dictOptionListQryExe;

	private final DictGetQryExe dictGetQryExe;

	private final DictListQryExe dictListQryExe;

	@Override
	public Result<Boolean> insert(DictInsertCmd cmd) {
		return dictInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> update(DictUpdateCmd cmd) {
		return dictUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> deleteById(DictDeleteCmd cmd) {
		return dictDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<DictCO> getById(DictGetQry qry) {
		return dictGetQryExe.execute(qry);
	}

	@Override
	public Result<List<OptionCO>> optionList(DictOptionListQry qry) {
		return dictOptionListQryExe.execute(qry);
	}

	@Override
	public Result<Datas<DictCO>> list(DictListQry qry) {
		return dictListQryExe.execute(qry);
	}

}
