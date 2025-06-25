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

package org.laokou.common.dubbo.rpc;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.oss.api.OssServiceI;
import org.laokou.oss.dto.OssUploadCmd;

/**
 * @author laokou
 */
@Slf4j
public class OssMock implements OssServiceI {

	@Override
	public Result<String> uploadOss(OssUploadCmd cmd) {
		log.error("调用上传文件失败，请检查Dubbo服务");
		throw new BizException("B_Dubbo_CallOssUploadFailed", "调用上传文件失败，请检查Dubbo服务");
	}

}
