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

package org.laokou.oss.ability;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.oss.gateway.OssGateway;
import org.laokou.oss.gateway.OssLogGateway;
import org.laokou.oss.model.OssA;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssDomainService {

	private final OssGateway ossGateway;

	private final OssLogGateway ossLogGateway;

	public void uploadOss(OssA ossA) {
		// 校验文件大小
		ossA.checkSize();
		// 校验扩展名
		ossA.checkExt();
		// 获取OSS信息
		ossA.getOssInfo(() -> ossLogGateway.getOssInfoByMd5(ossA.getMd5()), () -> ossGateway.uploadOssAndGetInfo(ossA));
	}

}
