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

package org.laokou.admin.operateLog.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.operateLog.convertor.OperateLogConvertor;
import org.laokou.admin.operateLog.dto.OperateLogPageQry;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.admin.operateLog.gatewayimpl.database.OperateLogMapper;
import org.laokou.admin.operateLog.gatewayimpl.database.dataobject.OperateLogDO;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 分页查询操作日志请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OperateLogPageQryExe {

	private final OperateLogMapper operateLogMapper;

	public Result<Page<OperateLogCO>> execute(OperateLogPageQry qry) {
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			CompletableFuture<List<OperateLogDO>> c1 = CompletableFuture
				.supplyAsync(() -> operateLogMapper.selectPageByCondition(qry), executor);
			CompletableFuture<Long> c2 = CompletableFuture
				.supplyAsync(() -> operateLogMapper.selectCountByCondition(qry), executor);
			return Result
				.ok(Page.create(c1.get(30, TimeUnit.SECONDS).stream().map(OperateLogConvertor::toClientObject).toList(),
						c2.get(30, TimeUnit.SECONDS)));
		}
		catch (Exception e) {
			throw new SystemException("S_OperateLog_PageQueryTimeout", "操作日志分页查询超时");
		}
	}

}
