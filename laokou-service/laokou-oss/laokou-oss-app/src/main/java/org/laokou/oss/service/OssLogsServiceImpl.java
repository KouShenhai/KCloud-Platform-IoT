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

package org.laokou.oss.service;

import lombok.RequiredArgsConstructor;
import org.laokou.oss.api.OssLogsServiceI;
import org.laokou.oss.command.OssLogSaveCmdExe;
import org.laokou.oss.dto.OssLogSaveCmd;
import org.springframework.stereotype.Service;

/**
 * OSS日志接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class OssLogsServiceImpl implements OssLogsServiceI {

	private final OssLogSaveCmdExe ossLogSaveCmdExe;

	@Override
	public void saveOssLog(OssLogSaveCmd cmd) {
		ossLogSaveCmdExe.executeVoid(cmd);
	}

}
