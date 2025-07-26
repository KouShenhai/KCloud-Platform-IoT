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

package org.laokou.oss.command;

import lombok.RequiredArgsConstructor;
import org.laokou.oss.ability.OssDomainService;
import org.laokou.oss.convertor.OssConvertor;
import org.laokou.oss.dto.OssUploadCmd;
import org.laokou.oss.dto.clientobject.OssUploadCO;
import org.laokou.oss.model.OssA;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssUploadCmdExe {

	private final OssDomainService ossDomainService;

	public OssUploadCO execute(OssUploadCmd cmd) {
		OssA ossA = OssConvertor.toEntity(cmd.getFileType(), cmd.getSize(), cmd.getExtName(), cmd.getBuffer(),
				cmd.getContentType(), cmd.getName());
		// 上传文件
		ossDomainService.uploadOss(ossA);
		return OssConvertor.toClientObject(ossA);
	}

}
