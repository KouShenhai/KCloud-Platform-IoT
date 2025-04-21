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

package org.laokou.common.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.FileUtils;
import org.springframework.boot.system.SystemProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

	private String testPath;

	@BeforeEach
	void setUp() {
		testPath = SystemProperties.get("user.home") + "/test";
	}

	@Test
	void testCreateAndDeleteFile() throws IOException {
		Path testFile = Path.of(testPath, "upload", "test.txt");

		// 创建文件
		assertDoesNotThrow(() -> FileUtils.create(testFile.getParent(), testFile));
		assertTrue(FileUtils.isExist(testFile));

		// 数据写入文件
		assertDoesNotThrow(() -> FileUtils.write(testFile, "test content".getBytes(StandardCharsets.UTF_8)));
		assertEquals("test content", Files.readString(testFile));

		// 删除文件
		assertDoesNotThrow(() -> FileUtils.delete(testFile));
		assertFalse(FileUtils.isExist(testFile));

		// 创建文件
		assertDoesNotThrow(() -> FileUtils.create(testFile.getParent().toString(), testFile.getFileName().toString()));
		assertTrue(FileUtils.isExist(testFile));

		// 删除文件
		assertDoesNotThrow(() -> FileUtils.delete(testFile.getParent().toString(), testFile.getFileName().toString()));
		assertFalse(FileUtils.isExist(testFile));
		assertFalse(FileUtils.deleteIfExists(testFile));
	}

	@Test
	void testCheckPathExists() {
		Path existingFile = Path.of(testPath, "upload", "existing.txt");
		// 创建文件
		assertDoesNotThrow(() -> {
			if (FileUtils.notExists(existingFile)) {
				FileUtils.createDirectories(existingFile.getParent());
				FileUtils.createFile(existingFile);
			}
		});
		assertTrue(FileUtils.isExist(existingFile));
		assertFalse(FileUtils.isExist(Path.of(testPath, "upload", "non_existent.txt")));
		assertDoesNotThrow(() -> FileUtils.delete(existingFile));
		assertTrue(FileUtils.notExists(existingFile));
	}

	@Test
	void testFileStreamOperations() throws IOException {
		Path streamFile = Path.of(testPath, "upload", "stream.txt");
		byte[] content = "stream data".getBytes(StandardCharsets.UTF_8);
		assertDoesNotThrow(() -> FileUtils.write(streamFile, content));
		byte[] readContent = FileUtils.getBytes(streamFile);
		assertArrayEquals(content, readContent);
		assertEquals("stream data", FileUtils.getStr(streamFile.toString()));
		assertDoesNotThrow(() -> FileUtils.delete(streamFile));
		assertTrue(Files.notExists(streamFile));
	}

	@Test
	void testZipOperations() throws IOException {
		// 测试多文件压缩场景
		Path srcFile1 = Path.of(testPath, "upload", "file1.txt");
		Path srcFile2 = Path.of(testPath, "upload", "file2.log");
		assertDoesNotThrow(() -> FileUtils.create(srcFile1.getParent(), srcFile1.getFileName()));
		assertDoesNotThrow(() -> FileUtils.create(srcFile2.getParent(), srcFile2.getFileName()));
		assertDoesNotThrow(() -> FileUtils.writeString(srcFile1, "文件1内容"));
		assertDoesNotThrow(() -> FileUtils.writeString(srcFile2, "文件2日志内容"));

		// 创建多文件ZIP压缩包
		Path multiZip = Path.of(testPath, "zip", "multi.zip");
		assertDoesNotThrow(() -> FileUtils.create(multiZip.getParent(), multiZip.getFileName()));
		assertDoesNotThrow(() -> FileUtils.zip(Path.of(testPath, "upload").toString(), multiZip.toString()));

		// 执行解压操作
		Path destDir = Path.of(testPath, "unzip");
		assertDoesNotThrow(() -> {
			if (FileUtils.notExists(destDir)) {
				FileUtils.createDirectories(destDir);
			}
		});
		assertDoesNotThrow(() -> FileUtils.unzip(multiZip, destDir));

		// 验证所有断言
		assertAll("ZIP操作综合测试", () -> assertTrue(FileUtils.isExist(multiZip), "多文件ZIP校验失败"), () -> {
			try (BufferedReader reader = Files.newBufferedReader(srcFile1)) {
				assertEquals("文件1内容", reader.readLine(), "文件1内容校验失败");
			}
			try (BufferedReader reader = Files.newBufferedReader(srcFile2)) {
				assertEquals("文件2日志内容", reader.readLine(), "文件2日志内容校验失败");
			}
		}, () -> assertTrue(FileUtils.isExist(Path.of(destDir.toString(), "upload", "file1.txt")), "解压文件1校验失败"),
				() -> assertTrue(FileUtils.isExist(Path.of(destDir.toString(), "upload", "file2.log")), "解压文件2校验失败"));
		assertTrue(FileUtils.deleteIfExists(multiZip));
		assertTrue(FileUtils.deleteIfExists(srcFile1));
		assertTrue(FileUtils.deleteIfExists(srcFile2));
		assertTrue(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file1.txt")));
		assertTrue(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file2.log")));
		assertDoesNotThrow(() -> FileUtils.delete(destDir.toString()));
		assertDoesNotThrow(() -> FileUtils.delete(multiZip.toString()));
		assertDoesNotThrow(() -> FileUtils.delete(Path.of(testPath, "upload").toString()));
		assertTrue(FileUtils.notExists(multiZip));
		assertTrue(FileUtils.notExists(destDir));
		assertTrue(FileUtils.notExists(Path.of(testPath, "upload")));
	}

}
