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

package org.laokou.admin.oss.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.oss.ability.OssDomainService;
import org.laokou.admin.oss.dto.OssRemoveCmd;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

/**
 * 删除OSS命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssRemoveCmdExe {

	private final OssDomainService ossDomainService;

	private final TransactionalUtil transactionalUtil;

	@CommandLog
	public void executeVoid(OssRemoveCmd cmd) {
		// 校验参数
		transactionalUtil.executeInTransaction(() -> ossDomainService.delete(cmd.getIds()));
	}

}
