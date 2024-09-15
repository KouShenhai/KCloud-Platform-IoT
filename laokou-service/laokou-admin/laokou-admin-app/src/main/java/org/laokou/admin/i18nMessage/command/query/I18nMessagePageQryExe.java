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

package org.laokou.admin.i18nMessage.command.query;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.i18nMessage.convertor.I18nMessageConvertor;
import org.laokou.admin.i18nMessage.dto.I18nMessagePageQry;
import org.laokou.admin.i18nMessage.dto.clientobject.I18nMessageCO;
import org.laokou.admin.i18nMessage.gatewayimpl.database.I18nMessageMapper;
import org.laokou.admin.i18nMessage.gatewayimpl.database.dataobject.I18nMessageDO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 分页查询国际化请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class I18nMessagePageQryExe {

	private final I18nMessageMapper i18nMessageMapper;

	private final Executor executor;

	@SneakyThrows
	public Result<Page<I18nMessageCO>> execute(I18nMessagePageQry qry) {
		CompletableFuture<List<I18nMessageDO>> c1 = CompletableFuture
			.supplyAsync(() -> i18nMessageMapper.selectPageByCondition(qry.index()), executor);
		CompletableFuture<Long> c2 = CompletableFuture.supplyAsync(() -> i18nMessageMapper.selectCountByCondition(qry),
				executor);
		return Result
			.ok(Page.create(c1.get(30, TimeUnit.SECONDS).stream().map(I18nMessageConvertor::toClientObject).toList(),
					c2.get(30, TimeUnit.SECONDS)));
	}

}
