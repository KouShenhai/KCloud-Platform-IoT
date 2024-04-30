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

package org.laokou.admin.command.message.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.gatewayimpl.database.MessageDetailMapper;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看未读消息数执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageUnreadCountGetQryExe {

	private final MessageDetailMapper messageDetailMapper;

	private final TransactionalUtil transactionalUtil;

	/**
	 * 执行查看未读消息数.
	 * @return 未读消息数
	 */
	@DS(TENANT)
	public Result<Integer> execute() {
		Integer count = transactionalUtil.defaultExecute(
				r -> messageDetailMapper.selectUnreadCountByUserId(UserUtil.getUserId()),
				TransactionDefinition.ISOLATION_READ_UNCOMMITTED, true);
		return Result.ok(count);
	}

}
