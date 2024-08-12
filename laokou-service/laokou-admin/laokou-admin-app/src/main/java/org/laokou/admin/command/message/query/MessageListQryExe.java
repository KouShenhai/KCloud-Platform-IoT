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

package org.laokou.admin.command.message.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.convertor.MessageConvertor;
import org.laokou.admin.dto.message.MessageListQry;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.gatewayimpl.database.MessageMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.admin.config.DsTenantProcessor.TENANT;

/**
 * 查询消息列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageListQryExe {

	private final MessageMapper messageMapper;

	private final Executor executor;

	private final MessageConvertor messageConvertor;

	/**
	 * 执行查询消息列表.
	 *
	 * @param qry 查询消息列表参数
	 * @return 消息列表
	 */
	@SneakyThrows
	@DS(TENANT)
	// @DataFilter(tableAlias = BOOT_SYS_MESSAGE)
	public Result<Datas<MessageCO>> execute(MessageListQry qry) {
		MessageDO messageDO = convert(qry);
		PageQuery page = qry;
		CompletableFuture<List<MessageDO>> c1 = CompletableFuture
			.supplyAsync(() -> messageMapper.selectListByCondition(messageDO, page), executor);
		CompletableFuture<Long> c2 = CompletableFuture
			.supplyAsync(() -> messageMapper.selectCountByCondition(messageDO, page), executor);
		CompletableFuture.allOf(List.of(c1, c2).toArray(CompletableFuture[]::new)).join();
		return Result.ok(Datas.create(c1.get().stream().map(messageConvertor::convertClientObj).toList(), c2.get()));
	}

	private MessageDO convert(MessageListQry qry) {
		MessageDO messageDO = new MessageDO();
		messageDO.setTitle(qry.getTitle());
		return messageDO;
	}

}
