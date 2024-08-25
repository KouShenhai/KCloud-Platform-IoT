// @formatter:off
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

package ${packageName}.${instanceName}.command.query;

import lombok.RequiredArgsConstructor;
import ${packageName}.${instanceName}.dto.${className}PageQry;
import ${packageName}.${instanceName}.dto.clientobject.${className}CO;
import ${packageName}.${instanceName}.gatewayimpl.database.${className}Mapper;
import ${packageName}.${instanceName}.gatewayimpl.database.dataobject.${className}DO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;
import ${packageName}.${instanceName}.convertor.${className}Convertor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 分页查询${comment}请求执行器.
 *
 * @author ${author}
 */
@Component
@RequiredArgsConstructor
public class ${className}PageQryExe {

	private final ${className}Mapper ${instanceName}Mapper;

	private final Executor executor;

	@SneakyThrows
	public Result<Page<${className}CO>> execute(${className}PageQry qry) {
		CompletableFuture<List<${className}DO>> c1 = CompletableFuture.supplyAsync(() -> ${instanceName}Mapper.selectPageByCondition(qry), executor);
		CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> ${instanceName}Mapper.selectCountByCondition(qry), executor);
		return Result.ok(Page.create(c1.get(30, TimeUnit.SECONDS).stream().map(${className}Convertor::toClientObject).toList(), c2.get(30, TimeUnit.SECONDS)));
	}

}
// @formatter:on
