/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.pack.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.packages.PackageGetQry;
import org.laokou.admin.dto.packages.clientobject.PackageCO;
import org.laokou.admin.gatewayimpl.database.PackageRepository;
import org.laokou.admin.gatewayimpl.database.PackageMenuRepository;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查看套餐执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class PackageGetQryExe {

	private final PackageRepository packageMapper;

	private final PackageMenuRepository packageMenuMapper;

	/**
	 * 执行查看套餐.
	 * @param qry 查看套餐参数
	 * @return 套餐
	 */
	public Result<PackageCO> execute(PackageGetQry qry) {
		return Result.of(convert(packageMapper.selectById(qry.getId())));
	}

	private PackageCO convert(PackageDO packageDO) {
		return PackageCO.builder()
			.id(packageDO.getId())
			.name(packageDO.getName())
			.menuIds(packageMenuMapper.selectMenuIdsByPackageId(packageDO.getId()))
			.build();
	}

}
