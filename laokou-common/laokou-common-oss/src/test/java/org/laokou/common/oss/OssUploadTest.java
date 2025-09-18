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

package org.laokou.common.oss;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ResourceUtils;
import org.laokou.common.oss.config.StorageAutoConfig;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.MinIO;
import org.laokou.common.oss.template.StorageTemplate;
import org.laokou.common.testcontainers.container.MinIOContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author laokou
 */
@ContextConfiguration(classes = {StorageAutoConfig.class })
@RequiredArgsConstructor
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OssUploadTest {

	private final StorageTemplate storageTemplate;

	private FileInfo fileInfo;

	static final MinIOContainer minIO = new MinIOContainer(DockerImageNames.minIO())
		.withUsername("minio")
		.withPassword("laokou123");

	@BeforeAll
	static void beforeAll() {
		minIO.start();
	}

	@AfterAll
	static void afterAll() {
		minIO.stop();
	}

	@BeforeEach
	void setUp() throws IOException {
		Resource resource = ResourceUtils.getResource("1.txt");
		InputStream inputStream = resource.getInputStream();
		File file = resource.getFile();
		long length = file.length();
		fileInfo = new FileInfo(inputStream, length, "text/plain", file.getName(), file.getName());
	}

	@Test
	void test() {
		MinIO minIO = new MinIO();
		minIO.setId(1L);
		minIO.setName("测试MinIO");
		minIO.setBucketName("laokou");
		// storageTemplate.uploadOss(fileInfo, List.of(minIO));
	}

}
