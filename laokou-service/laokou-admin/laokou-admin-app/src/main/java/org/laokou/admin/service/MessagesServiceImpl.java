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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.MessagesServiceI;
import org.laokou.admin.dto.message.*;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.command.message.MessageInsertCmdExe;
import org.laokou.admin.command.message.MessageReadCmdExe;
import org.laokou.admin.command.message.query.MessageGetQryExe;
import org.laokou.admin.command.message.query.MessageListQryExe;
import org.laokou.admin.command.message.query.MessageUnreadCountGetQryExe;
import org.laokou.admin.command.message.query.MessageUnreadListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesServiceI {

	private final MessageInsertCmdExe messageInsertCmdExe;

	private final MessageListQryExe messageListQryExe;

	private final MessageReadCmdExe messageReadCmdExe;

	private final MessageGetQryExe messageGetQryExe;

	private final MessageUnreadListQryExe messageUnreadListQryExe;

	private final MessageUnreadCountGetQryExe messageUnreadCountGetQryExe;

	@Override
	public Result<Boolean> insert(MessageInsertCmd cmd) {
		return messageInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<MessageCO>> list(MessageListQry qry) {
		return messageListQryExe.execute(qry);
	}

	@Override
	public Result<MessageCO> read(MessageReadCmd cmd) {
		return messageReadCmdExe.execute(cmd);
	}

	@Override
	public Result<MessageCO> getById(MessageGetQry qry) {
		return messageGetQryExe.execute(qry);
	}

	@Override
	public Result<Datas<MessageCO>> unreadList(MessageUnreadListQry qry) {
		return messageUnreadListQryExe.execute(qry);
	}

	@Override
	public Result<Integer> unreadCount(MessageUnreadCountGetQry qry) {
		return messageUnreadCountGetQryExe.execute(qry);
	}

}
