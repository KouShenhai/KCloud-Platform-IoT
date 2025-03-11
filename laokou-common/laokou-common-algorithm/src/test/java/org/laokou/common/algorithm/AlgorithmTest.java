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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.algorithm.template.Algorithm;
import org.laokou.common.algorithm.template.select.HashSelectAlgorithm;
import org.laokou.common.algorithm.template.select.PollSelectAlgorithm;
import org.laokou.common.algorithm.template.select.RandomSelectAlgorithm;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Random;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AlgorithmTest {

	@Test
	void testApi() {
		List<OssApi> ossApis = List.of(new TencentcloudOssApi(), new AliyunOssApi());
		// 负载均衡【哈希算法】
		Algorithm algorithm = new HashSelectAlgorithm();
		algorithm.select(ossApis, new Random().nextInt(10)).upload();
		// 负载均衡【轮询算法】
		algorithm = new PollSelectAlgorithm();
		algorithm.select(ossApis, "").upload();
		// 负载均衡【随机算法】
		algorithm = new RandomSelectAlgorithm();
		algorithm.select(ossApis, "").upload();
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
