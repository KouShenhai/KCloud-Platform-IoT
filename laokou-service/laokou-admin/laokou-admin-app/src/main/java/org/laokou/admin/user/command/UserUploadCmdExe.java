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

package org.laokou.admin.user.command;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.dto.UserUploadAvatarCmd;
import org.laokou.admin.user.gatewayimpl.rpc.OssWrapperRpc;
import org.laokou.common.core.util.FileUtils;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.i18n.dto.Result;
import org.laokou.oss.dto.OssUploadCmd;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserUploadCmdExe {

	private final OssWrapperRpc ossWrapperRpc;

	@CommandLog
	public Result<String> execute(UserUploadAvatarCmd cmd) throws IOException, NoSuchAlgorithmException {
		MultipartFile file = cmd.getFile();
		String name = file.getOriginalFilename();
		Assert.notNull(name, "文件名不能为空");
		String extName = FileUtils.getFileExt(name);
		return ossWrapperRpc.uploadOss(new OssUploadCmd("image", file.getBytes(),
				UUIDGenerator.generateUUID() + extName, extName, file.getContentType(), file.getSize()));
	}

}
