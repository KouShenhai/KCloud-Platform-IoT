/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.oss.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.convertor.OssConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.oss.OssListQry;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.admin.gatewayimpl.database.OssMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OssDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_OSS;
import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查询OSS列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssListQryExe {

	private final OssMapper ossMapper;

	private final Executor executor;

	private final OssConvertor ossConvertor;

	/**
	 * 执行查询OSS列表.
	 * @param qry 查询OSS列表参数
	 * @return OSS列表
	 */
	@DS(TENANT)
	@SneakyThrows
	@DataFilter(tableAlias = BOOT_SYS_OSS)
	public Result<Datas<OssCO>> execute(OssListQry qry) {
		OssDO ossDO = new OssDO(qry.getName());
		PageQuery page = qry.page();
		CompletableFuture<List<OssDO>> c1 = CompletableFuture
			.supplyAsync(() -> ossMapper.selectListByCondition(ossDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> ossMapper.selectCountByCondition(ossDO, page),
				executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.to(c1.get().stream().map(ossConvertor::convertClientObj).toList(), c2.get()));
	}

}
