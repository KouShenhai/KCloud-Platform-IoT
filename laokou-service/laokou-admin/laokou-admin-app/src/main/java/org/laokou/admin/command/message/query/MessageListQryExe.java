/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.MessageGateway;
import org.laokou.admin.domain.message.Message;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.message.MessageListQry;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageListQryExe {

	private final MessageGateway messageGateway;

	public Result<Datas<MessageCO>> execute(MessageListQry qry) {
		Message message = ConvertUtil.sourceToTarget(qry, Message.class);
		Datas<Message> list = messageGateway.list(message, toUser(), qry);
		Datas<MessageCO> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(list.getRecords(), MessageCO.class));
		datas.setTotal(list.getTotal());
		return Result.of(datas);
	}

	private User toUser() {
		return new User(UserUtil.getTenantId());
	}

}