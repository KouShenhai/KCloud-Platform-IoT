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
import lombok.SneakyThrows;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.packages.PackageListQry;
import org.laokou.admin.dto.packages.clientobject.PackageCO;
import org.laokou.admin.gatewayimpl.database.PackageRepository;
import org.laokou.admin.gatewayimpl.database.dataobject.PackageDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_PACKAGE;

/**
 * 查询套餐列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class PackageListQryExe {

	private final PackageRepository packageMapper;

	private final Executor executor;

	/**
	 * 执行查询套餐列表.
	 * @param qry 查询套餐列表
	 * @return 套餐列表
	 */
	@SneakyThrows
	@DataFilter(tableAlias = BOOT_SYS_PACKAGE)
	public Result<Datas<PackageCO>> execute(PackageListQry qry) {
		PackageDO packageDO = convert(qry);
		PageQuery page = qry.page();
		CompletableFuture<List<PackageDO>> c1 = CompletableFuture
			.supplyAsync(() -> packageMapper.selectListByCondition(packageDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> packageMapper.selectCountByCondition(packageDO, page), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.of(Datas.of(c1.get().stream().map(this::convert).toList(), c2.get()));
	}

	private PackageDO convert(PackageListQry qry) {
		PackageDO packageDO = new PackageDO();
		packageDO.setName(qry.getName());
		return packageDO;
	}

	private PackageCO convert(PackageDO packageDO) {
		return PackageCO.builder().id(packageDO.getId()).name(packageDO.getName()).build();
	}

}
