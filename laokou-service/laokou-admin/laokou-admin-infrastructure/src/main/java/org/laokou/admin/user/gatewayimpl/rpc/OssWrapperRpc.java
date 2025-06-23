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

package org.laokou.admin.user.gatewayimpl.rpc;

import org.apache.dubbo.config.annotation.DubboReference;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.oss.api.OssServiceI;
import org.laokou.oss.dto.OssUploadCmd;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
@Component
public class OssWrapperRpc {

	@DubboReference(group = "iot", version = "v3", interfaceClass = OssServiceI.class,
			mock = "org.laokou.admin.user.gatewayimpl.rpc.OssServiceIMock", loadbalance = "adaptive", retries = 3)
	private OssServiceI ossServiceI;

	public Result<String> uploadOss(OssUploadCmd cmd) throws IOException, NoSuchAlgorithmException {
		Result<String> result = ossServiceI.uploadOss(cmd);
		if (result.success()) {
			return result;
		}
		throw new BizException(result.getCode(), result.getMsg());
	}

}
