/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.i18nMessage.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.i18nMessage.ability.I18nMessageDomainService;
import org.laokou.admin.i18nMessage.convertor.I18nMessageConvertor;
import org.laokou.admin.i18nMessage.dto.I18nMessageSaveCmd;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

/**
 * 保存国际化消息命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class I18nMessageSaveCmdExe {

	private final I18nMessageDomainService i18nMessageDomainService;

	private final TransactionalUtil transactionalUtil;

	@CommandLog
	public void executeVoid(I18nMessageSaveCmd cmd) {
		// 校验参数
		transactionalUtil
			.executeInTransaction(() -> i18nMessageDomainService.create(I18nMessageConvertor.toEntity(cmd.getCo())));
	}

}
