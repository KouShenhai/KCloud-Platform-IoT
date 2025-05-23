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
import org.laokou.admin.oss.convertor.OssConvertor;
import org.laokou.admin.oss.dto.OssSaveCmd;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.springframework.stereotype.Component;

/**
 * 保存OSS命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssSaveCmdExe {

	private final OssDomainService ossDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(OssSaveCmd cmd) {
		// 校验参数
		transactionalUtils.executeInTransaction(() -> ossDomainService.createOss(OssConvertor.toEntity(cmd.getCo())));
	}

}
