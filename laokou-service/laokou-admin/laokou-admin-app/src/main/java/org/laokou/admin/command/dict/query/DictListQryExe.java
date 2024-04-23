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

package org.laokou.admin.command.dict.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.dto.dict.DictListQry;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.DatasourceConstant.BOOT_SYS_DICT;
import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查询部门列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictListQryExe {

	private final DictMapper dictMapper;

	private final Executor executor;

	/**
	 * 执行查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	@SneakyThrows
	@DS(TENANT)
	@DataFilter(tableAlias = BOOT_SYS_DICT)
	public Result<Datas<DictCO>> execute(DictListQry qry) {
		DictDO dictDO = convert(qry);
		PageQuery page = qry.page();
		CompletableFuture<List<DictDO>> c1 = CompletableFuture
			.supplyAsync(() -> dictMapper.selectListByCondition(dictDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> dictMapper.selectCountByCondition(dictDO, page), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.of(c1.get().stream().map(this::convert).toList(), c2.get()));
	}

	private DictDO convert(DictListQry qry) {
		DictDO dictDO = new DictDO();
		dictDO.setLabel(qry.getLabel());
		dictDO.setType(qry.getType());
		return dictDO;
	}

	private DictCO convert(DictDO dictDO) {
		return DictCO.builder()
			.id(dictDO.getId())
			.value(dictDO.getValue())
			.label(dictDO.getLabel())
			.type(dictDO.getType())
			.createDate(dictDO.getCreateDate())
			.remark(dictDO.getRemark())
			.sort(dictDO.getSort())
			.build();
	}

}
