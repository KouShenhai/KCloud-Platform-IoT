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

package org.laokou.oss;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.FileUtils;
import org.springframework.boot.system.SystemProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OssTest {

	@BeforeEach
	void setUp() throws IOException {
		String testPath = SystemProperties.get("user.home") + "/test";
		Path testFile = Path.of(testPath, "upload", "test.txt");

		// 创建文件
		assertDoesNotThrow(() -> FileUtils.create(testFile.getParent(), testFile));
		assertTrue(FileUtils.isExist(testFile));

		// 数据写入文件
		assertDoesNotThrow(() -> FileUtils.write(testFile, "test content".getBytes(StandardCharsets.UTF_8)));
		assertEquals("test content", Files.readString(testFile));
	}

	@Test
	void testLocalUpload() {

	}

}
