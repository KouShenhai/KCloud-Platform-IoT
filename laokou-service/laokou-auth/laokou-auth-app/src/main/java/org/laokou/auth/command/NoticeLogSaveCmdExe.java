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

package org.laokou.auth.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.convertor.NoticeLogConvertor;
import org.laokou.auth.dto.NoticeLogSaveCmd;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class NoticeLogSaveCmdExe {

	private final DomainService domainService;

	private final TransactionalUtils transactionalUtils;

	@Async
	@CommandLog
	public void executeVoid(NoticeLogSaveCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			transactionalUtils
				.executeInTransaction(() -> domainService.createNoticeLog(NoticeLogConvertor.toEntity(cmd.getCo())));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
