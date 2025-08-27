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

package org.laokou.admin.oss.gatewayimpl.rpc;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.laokou.admin.oss.convertor.OssConvertor;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.oss.api.OssServiceI;
import org.laokou.oss.api.OssUploadCO;
import org.laokou.oss.api.OssUploadResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static org.laokou.common.i18n.common.exception.StatusCode.OK;
import static org.laokou.common.mybatisplus.mapper.BaseDO.CREATOR;
import static org.laokou.common.mybatisplus.mapper.BaseDO.TENANT_ID;

/**
 * @author laokou
 */
@Component
public class OssRpc {

	@DubboReference(group = "iot-oss", version = "v3", interfaceClass = OssServiceI.class,
			mock = "org.laokou.admin.oss.gatewayimpl.rpc.OssMock", loadbalance = "adaptive", retries = 3)
	private OssServiceI ossServiceI;

	public OssUploadCO uploadOss(MultipartFile file, String fileType) throws Exception {
		RpcContextAttachment serverAttachment = RpcContext.getServerAttachment();
		serverAttachment.setAttachment(TENANT_ID, UserUtils.getTenantId());
		serverAttachment.setAttachment(CREATOR, UserUtils.getUserId());
		OssUploadResult result = ossServiceI.uploadOss(OssConvertor.toAssembler(file, fileType));
		if (ObjectUtils.equals(OK, result.getCode())) {
			return result.getData();
		}
		throw new BizException(result.getCode(), result.getMsg());
	}

}
