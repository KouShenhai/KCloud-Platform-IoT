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
import org.laokou.admin.api.PackagesServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.packages.*;
import org.laokou.admin.dto.packages.clientobject.PackageCO;
import org.laokou.admin.command.packages.PackageDeleteCmdExe;
import org.laokou.admin.command.packages.PackageInsertCmdExe;
import org.laokou.admin.command.packages.PackageUpdateCmdExe;
import org.laokou.admin.command.packages.query.PackageGetQryExe;
import org.laokou.admin.command.packages.query.PackageListQryExe;
import org.laokou.admin.command.packages.query.PackageOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class PackagesServiceImpl implements PackagesServiceI {

	private final PackageInsertCmdExe packageInsertCmdExe;

	private final PackageUpdateCmdExe packageUpdateCmdExe;

	private final PackageListQryExe packageListQryExe;

	private final PackageDeleteCmdExe packageDeleteCmdExe;

	private final PackageGetQryExe packageGetQryExe;

	private final PackageOptionListQryExe packageOptionListQryExe;

	@Override
	public Result<Boolean> insert(PackageInsertCmd cmd) {
		return packageInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> update(PackageUpdateCmd cmd) {
		return packageUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> deleteById(PackageDeleteCmd cmd) {
		return packageDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<PackageCO>> list(PackageListQry qry) {
		return packageListQryExe.execute(qry);
	}

	@Override
	public Result<PackageCO> getById(PackageGetQry qry) {
		return packageGetQryExe.execute(qry);
	}

	@Override
	public Result<List<OptionCO>> optionList(PackageOptionListQry qry) {
		return packageOptionListQryExe.execute();
	}

}
