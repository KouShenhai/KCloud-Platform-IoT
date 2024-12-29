/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.convertor.LoginLogConvertor;
import org.laokou.auth.dto.LoginLogSaveCmd;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.laokou.common.core.config.SpringTaskExecutorConfig.THREAD_POOL_TASK_EXECUTOR_NAME;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogSaveCmdExe {

	private final DomainService domainService;

	private final TransactionalUtil transactionalUtil;

	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	public void executeVoid(LoginLogSaveCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push("domain");
			transactionalUtil
				.executeInTransaction(() -> domainService.createLoginLog(LoginLogConvertor.toEntity(cmd.getCo())));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
