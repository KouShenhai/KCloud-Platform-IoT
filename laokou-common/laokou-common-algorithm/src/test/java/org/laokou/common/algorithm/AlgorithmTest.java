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

package org.laokou.common.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class AlgorithmTest {

	@Test
	void testApi() {
		List<OssApi> ossApis = List.of(new TencentcloudOssApi(), new TencentcloudOssApi());
		// 负载均衡【】
	}

	@Test
	void testServiceInstance() {

	}

	interface OssApi {

		void upload();

	}

	static class AliyunOssApi implements OssApi {

		@Override
		public void upload() {
			log.info("阿里云OSS => 上传文件成功");
		}

	}

	static class TencentcloudOssApi implements OssApi {

		@Override
		public void upload() {
			log.info("腾讯云OSS => 上传文件成功");
		}

	}

}
