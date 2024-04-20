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

package org.laokou.admin.command.ip.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

	/**
	 * 查询IP列表.
	 * @param qry 查询IP列表参数
	 * @return IP列表
	 */
	@SneakyThrows
	public Result<Datas<IpCO>> execute(IpListQry qry) {
		IpDO ipDO = convert(qry.getLabel());
		PageQuery page = qry.page();
		CompletableFuture<List<IpDO>> c1 = CompletableFuture
			.supplyAsync(() -> ipMapper.selectListByCondition(ipDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> ipMapper.selectCountByCondition(ipDO, page),
				executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.of(Datas.of(c1.get().stream().map(this::convert).toList(), c2.get()));
	}

	private IpDO convert(String label) {
		IpDO ipDO = new IpDO();
		ipDO.setLabel(label);
		return ipDO;
	}

	private IpCO convert(IpDO ipDO) {
		return IpCO.builder().id(ipDO.getId()).value(ipDO.getValue()).build();
	}

}
