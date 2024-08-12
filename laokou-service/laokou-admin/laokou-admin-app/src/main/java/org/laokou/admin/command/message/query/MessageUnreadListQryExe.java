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
import org.laokou.admin.convertor.MessageConvertor;
import org.laokou.admin.dto.message.MessageUnreadListQry;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.gatewayimpl.database.MessageMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;

import java.util.List;

import static org.laokou.admin.config.DsTenantProcessor.TENANT;

/**
 * 查询未读消息列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageUnreadListQryExe {

	private final MessageMapper messageMapper;

	private final TransactionalUtil transactionalUtil;

	private final MessageConvertor messageConvertor;

	/**
	 * 执行查询未读消息列表.
	 * @param qry 查询未读消息列表参数
	 * @return 未读消息列表
	 */
	@DS(TENANT)
	public Result<Datas<MessageCO>> execute(MessageUnreadListQry qry) {
		PageQuery pageQuery = qry;
		List<MessageDO> list = transactionalUtil.defaultExecute(
				r -> messageMapper.selectUnreadListByCondition(UserUtil.getUserId(), qry.getType(), pageQuery),
				TransactionDefinition.ISOLATION_READ_UNCOMMITTED, true);
		Long count = transactionalUtil.defaultExecute(
				r -> messageMapper.selectUnreadCountByCondition(UserUtil.getUserId(), qry.getType(), pageQuery),
				TransactionDefinition.ISOLATION_READ_UNCOMMITTED, true);
		return Result.ok(Datas.create(list.stream().map(messageConvertor::convertClientObj).toList(), count));
	}

}
