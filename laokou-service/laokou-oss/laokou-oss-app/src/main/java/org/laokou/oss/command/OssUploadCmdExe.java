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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.oss.template.StorageTemplate;
import org.laokou.oss.convertor.OssConvertor;
import org.laokou.oss.dto.OssUploadCmd;
import org.laokou.oss.gatewayimpl.database.OssMapper;
import org.laokou.oss.gatewayimpl.database.dataobject.OssDO;
import org.laokou.oss.model.OssA;
import org.laokou.oss.model.OssStatusEnum;
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

	private final OssMapper ossMapper;

	public Result<String> execute(OssUploadCmd cmd) throws IOException, NoSuchAlgorithmException {
		OssA ossA = OssConvertor.toEntity(cmd.getFileType(), cmd.getSize(), cmd.getExtName());
		// 校验文件大小
		ossA.checkSize();
		// 校验扩展名
		ossA.checkExt();
		return storageTemplate.uploadOss(
				OssConvertor.to(cmd.getBuffer(), cmd.getSize(), cmd.getContentType(), cmd.getName(), cmd.getExtName()),
				OssConvertor.tos(ossMapper.selectList(Wrappers.lambdaQuery(OssDO.class)
					.eq(OssDO::getStatus, OssStatusEnum.ENABLE.getCode())
					.select(OssDO::getParam, OssDO::getType))));
	}

}
