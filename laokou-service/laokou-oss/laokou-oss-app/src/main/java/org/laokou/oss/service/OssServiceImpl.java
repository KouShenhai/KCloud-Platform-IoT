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
import org.laokou.common.i18n.dto.Result;
import org.laokou.oss.api.OssServiceI;
import org.laokou.oss.command.OssUploadCmdExe;
import org.laokou.oss.dto.OssUploadCmd;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssServiceI {

	private final OssUploadCmdExe ossUploadCmdExe;

	@Override
	public Result<String> upload(OssUploadCmd cmd) throws IOException, NoSuchAlgorithmException {
		return ossUploadCmdExe.execute(cmd);
	}

}
