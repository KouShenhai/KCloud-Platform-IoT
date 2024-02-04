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
import org.laokou.admin.convertor.MessageConvertor;
import org.laokou.admin.domain.gateway.MessageGateway;
import org.laokou.admin.domain.message.Message;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.dto.message.MessageCreateCmd;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 新增消息执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageCreateCmdExe {

	private final MessageGateway messageGateway;

	private final MessageConvertor messageConvertor;

	/**
	 * 执行新增消息.
	 * @param cmd 新增消息参数
	 * @return 执行新增结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(MessageCreateCmd cmd) {
		return Result.of(messageGateway.insert(toMessage(cmd.getMessageCO()), toUser()));
	}

	/**
	 * 转换成用户领域.
	 * @return 用户领域
	 */
	private User toUser() {
		return ConvertUtil.sourceToTarget(UserUtil.user(), User.class);
	}

	/**
	 * 转换成消息领域.
	 * @param co 客户端消息通信对象
	 * @return 消息领域
	 */
	private Message toMessage(MessageCO co) {
		return messageConvertor.toEntity(co);
	}

}
