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

package org.laokou.auth.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.laokou.auth.dto.log.domainevent.LoginLogEvent;
import org.laokou.auth.gatewayimpl.database.LoginLogMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginLogHandler implements ApplicationListener<LoginLogEvent> {

	private final LoginLogMapper loginLogMapper;
	private final ThreadPoolTaskExecutor taskExecutor;
	private final TransactionalUtil transactionalUtil;

	@Override
	public void onApplicationEvent(@NotNull LoginLogEvent event) {
		CompletableFuture.runAsync(() -> transactionalUtil.executeWithoutResult(callback -> {
			try {
				execute(event);
			}
			catch (Exception e) {
				callback.setRollbackOnly();
				log.error("数据插入失败已回滚，错误信息：{}", e.getMessage());
			}
		}), taskExecutor);
	}

	private void execute(LoginLogEvent event) {
		LoginLogDO logDO = ConvertUtil.sourceToTarget(event, LoginLogDO.class);
		loginLogMapper.insert(logDO);
	}

}
