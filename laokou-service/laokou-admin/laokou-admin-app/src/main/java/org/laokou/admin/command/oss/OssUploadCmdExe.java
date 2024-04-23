/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.oss;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domain.gateway.OssGateway;
import org.laokou.admin.domain.oss.File;
import org.laokou.admin.dto.oss.OssUploadCmd;
import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * OSS上传文件执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssUploadCmdExe {

	private final OssGateway ossGateway;

	/**
	 * 执行OSS上传文件.
	 * @param cmd OSS上传文件参数
	 * @return 执行上传文件结果
	 */
	@DS(TENANT)
	public Result<FileCO> execute(OssUploadCmd cmd) {
		return Result.ok(convert(ossGateway.upload(cmd.getFile())));
	}

	private FileCO convert(File file) {
		return FileCO.builder().md5(file.getMd5()).url(file.getUrl()).build();
	}

}
