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
import org.laokou.admin.convertor.MessageConvertor;
import org.laokou.admin.dto.message.MessageGetQry;
import org.laokou.admin.dto.message.clientobject.MessageCO;
import org.laokou.admin.gatewayimpl.database.MessageMapper;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看消息执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageGetQryExe {

	private final MessageMapper messageMapper;

	private final MessageConvertor messageConvertor;

	/**
	 * 执行查看消息.
	 * @param qry 查看消息参数
	 * @return 消息
	 */
	@DS(TENANT)
	public Result<MessageCO> execute(MessageGetQry qry) {
		return Result.ok(messageConvertor.convertClientObj(messageMapper.selectById(qry.getId())));
	}

}
