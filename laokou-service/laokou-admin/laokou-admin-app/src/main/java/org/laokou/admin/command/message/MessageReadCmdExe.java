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

package org.laokou.admin.command.message;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domain.message.Read;
import org.laokou.admin.dto.message.MessageReadCmd;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.gatewayimpl.database.MessageDetailMapper;
import org.laokou.admin.gatewayimpl.database.MessageMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDetailDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.Constant.TENANT;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReadCmdExe {

	private final MessageDetailMapper messageDetailMapper;

	private final MessageMapper messageMapper;

	private final TransactionalUtil transactionalUtil;

	@DS(TENANT)
	public Result<MessageCO> execute(MessageReadCmd cmd) {
		Long detailId = cmd.getDetailId();
		updateFlag(detailId);
		MessageDO list = messageMapper.getMessageByDetailId(detailId);
		return Result.of(ConvertUtil.sourceToTarget(list, MessageCO.class));
	}

	private void updateFlag(Long id) {
		MessageDetailDO messageDetailDO = new MessageDetailDO();
		messageDetailDO.setId(id);
		// 0未读 1已读
		messageDetailDO.setReadFlag(Read.YES.ordinal());
		messageDetailDO.setVersion(messageDetailMapper.getVersion(id, MessageDetailDO.class));
		transactionalUtil.executeWithoutResult(rollback -> {
			try {
				messageDetailMapper.updateById(messageDetailDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				rollback.setRollbackOnly();
			}
		});
	}

}
