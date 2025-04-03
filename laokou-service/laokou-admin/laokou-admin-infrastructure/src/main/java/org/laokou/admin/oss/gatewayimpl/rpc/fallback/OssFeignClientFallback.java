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

package org.laokou.admin.oss.gatewayimpl.rpc.fallback;

import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.oss.gatewayimpl.rpc.OssFeignClient;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.dto.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author laokou
 */
@Slf4j
public class OssFeignClientFallback implements OssFeignClient {

	private final Throwable cause;

	public OssFeignClientFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public Result<String> uploadV3(MultipartFile file) {
		log.error("文件上传失败，错误信息：{}", cause.getMessage(), cause);
		throw new BizException("B_Oss_UploadFailed", "文件上传失败，服务正在维护，请联系管理员", cause);
	}

}
