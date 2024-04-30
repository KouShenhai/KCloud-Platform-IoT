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

package org.laokou.admin.command.ip.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.convertor.IpConvertor;
import org.laokou.admin.dto.ip.IpListQry;
import org.laokou.admin.dto.ip.clientobject.IpCO;
import org.laokou.admin.gatewayimpl.database.IpMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.IpDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 查询IP列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class IpListQryExe {

	private final IpMapper ipMapper;

	private final Executor executor;

	private final IpConvertor ipConvertor;

	/**
	 * 查询IP列表.
	 * @param qry 查询IP列表参数
	 * @return IP列表
	 */
	@SneakyThrows
	public Result<Datas<IpCO>> execute(IpListQry qry) {
		IpDO ipDO = new IpDO(qry.getLabel());
		PageQuery page = qry.page();
		CompletableFuture<List<IpDO>> c1 = CompletableFuture
			.supplyAsync(() -> ipMapper.selectListByCondition(ipDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> ipMapper.selectCountByCondition(ipDO, page),
				executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.to(c1.get().stream().map(ipConvertor::convertClientObj).toList(), c2.get()));
	}

}
