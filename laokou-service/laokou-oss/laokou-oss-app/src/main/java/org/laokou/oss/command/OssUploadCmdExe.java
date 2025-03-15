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
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.oss.entity.OssInfo;
import org.laokou.common.oss.entity.Type;
import org.laokou.common.oss.template.StorageTemplate;
import org.laokou.oss.dto.OssUploadCmd;
import org.laokou.oss.model.OssE;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssUploadCmdExe {

	private final StorageTemplate storageTemplate;

	public Result<String> execute(OssUploadCmd cmd) throws IOException, NoSuchAlgorithmException {
		OssE ossE = new OssE(cmd.getFile());
		// 校验文件大小
		ossE.checkSize();
		// 校验扩展名
		ossE.checkExt();
		return storageTemplate.upload(ossE, getInfo());
	}

	// TODO 从数据库获取配置，根据系统设置进行负载均衡
	private OssInfo getInfo() {
		OssInfo ossInfo = new OssInfo();
		ossInfo.setType(Type.LOCAL);
		ossInfo.setDomain("http://localhost:82");
		ossInfo.setDirectory("D:\\laokou\\temp");
		ossInfo.setPath("/temp/");
		return ossInfo;
	}

}
