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

package org.laokou.admin.command.source.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.source.SourceListQry;
import org.laokou.admin.dto.source.clientobject.SourceCO;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_SOURCE;

/**
 * 查询数据源列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceListQryExe {

	private final SourceMapper sourceMapper;

	private final Executor executor;

	/**
	 * 执行查询数据源列表.
	 * @param qry 查询数据源列表参数
	 * @return 数据源列表
	 */
	@SneakyThrows
	@DataFilter(tableAlias = BOOT_SYS_SOURCE)
	public Result<Datas<SourceCO>> execute(SourceListQry qry) {
		SourceDO sourceDO = convert(qry);
		PageQuery page = qry.page();
		CompletableFuture<List<SourceDO>> c1 = CompletableFuture
			.supplyAsync(() -> sourceMapper.selectListByCondition(sourceDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> sourceMapper.selectCountByCondition(sourceDO, page), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.of(c1.get().stream().map(this::convert).toList(), c2.get()));
	}

	private SourceDO convert(SourceListQry qry) {
		SourceDO sourceDO = new SourceDO();
		sourceDO.setName(qry.getName());
		return sourceDO;
	}

	private SourceCO convert(SourceDO sourceDO) {
		return SourceCO.builder()
			.id(sourceDO.getId())
			.name(sourceDO.getName())
			.url(sourceDO.getUrl())
			.driverClassName(sourceDO.getDriverClassName())
			.username(sourceDO.getUsername())
			.build();
	}

}
