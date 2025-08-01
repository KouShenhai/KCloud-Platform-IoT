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
import org.apache.dubbo.config.annotation.DubboService;
import org.laokou.common.i18n.dto.Result;
import org.laokou.oss.api.DubboOssServiceITriple;
import org.laokou.oss.api.OssUploadCmd;
import org.laokou.oss.api.OssUploadResult;
import org.laokou.oss.command.OssUploadCmdExe;
import org.laokou.oss.convertor.OssConvertor;
import org.laokou.oss.dto.clientobject.OssUploadCO;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@DubboService(token = "0e02b2c3d479", group = "iot-oss", version = "v3", timeout = 10000)
@RequiredArgsConstructor
public class OssServiceImpl extends DubboOssServiceITriple.OssServiceIImplBase {

	private final OssUploadCmdExe ossUploadCmdExe;

	@Override
	public OssUploadResult uploadOss(OssUploadCmd cmd) {
		Result<OssUploadCO> result = ossUploadCmdExe.execute(OssConvertor.toAssembler(cmd));
		return OssUploadResult.newBuilder()
			.setCode(result.getCode())
			.setMsg(result.getMsg())
			.setData(OssConvertor.toClientObject(result.getData()))
			.build();
	}

}
