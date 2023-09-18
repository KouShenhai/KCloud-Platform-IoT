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
import org.laokou.admin.api.OssServiceI;
import org.laokou.admin.dto.oss.*;
import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.admin.command.oss.*;
import org.laokou.admin.command.oss.query.OssGetQryExe;
import org.laokou.admin.command.oss.query.OssListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssServiceI {

	private final OssInsertCmdExe ossInsertCmdExe;

	private final OssUpdateCmdExe ossUpdateCmdExe;

	private final OssDeleteCmdExe ossDeleteCmdExe;

	private final OssListQryExe ossListQryExe;

	private final OssUploadCmdExe ossUploadCmdExe;

	private final OssGetQryExe ossGetQryExe;

	@Override
	public Result<Boolean> insert(OssInsertCmd cmd) {
		return ossInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> update(OssUpdateCmd cmd) {
		return ossUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> deleteById(OssDeleteCmd cmd) {
		return ossDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<OssCO> getById(OssGetQry qry) {
		return ossGetQryExe.execute(qry);
	}

	@Override
	public Result<Datas<OssCO>> list(OssListQry qry) {
		return ossListQryExe.execute(qry);
	}

	@Override
	public Result<FileCO> upload(OssUploadCmd cmd) {
		return ossUploadCmdExe.execute(cmd);
	}

}
