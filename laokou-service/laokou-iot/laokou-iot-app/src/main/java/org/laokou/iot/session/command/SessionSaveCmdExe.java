/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.session.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.session.ability.SessionDomainService;
import org.laokou.iot.session.convertor.SessionConvertor;
import org.laokou.iot.session.dto.SessionSaveCmd;
import org.laokou.iot.session.factory.SessionDomainFactory;
import org.laokou.iot.session.model.SessionA;
import org.springframework.stereotype.Component;

/**
 * 保存会话命令执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionSaveCmdExe {

	private final SessionDomainService sessionDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(SessionSaveCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			SessionA sessionA = SessionDomainFactory.createSessionA()
				.create(SessionConvertor.toEntity(cmd.getCo()));
			// 校验参数
			sessionA.checkSessionParam();
			transactionalUtils.executeInTransaction(() -> sessionDomainService.createSession(sessionA));
		}
		catch (Exception ex) {
			log.error("保存会话失败，错误信息：{}", ex.getMessage(), ex);
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
