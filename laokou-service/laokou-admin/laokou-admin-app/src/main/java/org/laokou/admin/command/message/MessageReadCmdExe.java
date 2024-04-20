/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.admin.domain.gateway.MessageGateway;
import org.laokou.admin.domain.message.MessageDetail;
import org.laokou.admin.dto.message.MessageReadCmd;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.gatewayimpl.database.MessageRepository;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;
import static org.laokou.common.i18n.common.MessageReadEnum.YES;

/**
 * 读取消息执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReadCmdExe {

	private final MessageGateway messageGateway;

	private final MessageRepository messageMapper;

	/**
	 * 执行读取消息.
	 * @param cmd 读取消息参数
	 * @return 消息
	 */
	@DS(TENANT)
	public Result<MessageCO> execute(MessageReadCmd cmd) {
		Long detailId = cmd.getDetailId();
		messageGateway.read(convert(detailId));
		return Result.of(convert(messageMapper.selectByDetailId(detailId)));
	}

	private MessageDetail convert(Long detailId) {
		return MessageDetail.builder().id(detailId).readFlag(YES.ordinal()).build();
	}

	private MessageCO convert(MessageDO messageDO) {
		return MessageCO.builder().title(messageDO.getTitle()).content(messageDO.getContent()).build();
	}

}
