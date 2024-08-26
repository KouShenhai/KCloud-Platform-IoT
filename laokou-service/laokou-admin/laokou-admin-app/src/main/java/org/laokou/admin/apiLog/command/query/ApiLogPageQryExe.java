/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.apiLog.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.apiLog.dto.ApiLogPageQry;
import org.laokou.admin.apiLog.dto.clientobject.ApiLogCO;
import org.laokou.admin.apiLog.gatewayimpl.database.ApiLogMapper;
import org.laokou.admin.apiLog.gatewayimpl.database.dataobject.ApiLogDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;
import org.laokou.admin.apiLog.convertor.ApiLogConvertor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 分页查询Api日志请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ApiLogPageQryExe {

	private final ApiLogMapper apiLogMapper;

	private final Executor executor;

	@SneakyThrows
	public Result<Page<ApiLogCO>> execute(ApiLogPageQry qry) {
		CompletableFuture<List<ApiLogDO>> c1 = CompletableFuture
			.supplyAsync(() -> apiLogMapper.selectPageByCondition(qry), executor);
		CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> apiLogMapper.selectCountByCondition(qry),
				executor);
		return Result
			.ok(Page.create(c1.get(30, TimeUnit.SECONDS).stream().map(ApiLogConvertor::toClientObject).toList(),
					c2.get(30, TimeUnit.SECONDS)));
	}

}
