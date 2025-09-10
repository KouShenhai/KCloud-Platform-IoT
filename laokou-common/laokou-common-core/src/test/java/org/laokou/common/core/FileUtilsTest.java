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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.FileUtils;
import org.springframework.boot.system.SystemProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class FileUtilsTest {

	private String testPath;

	@BeforeEach
	void setUp() {
		testPath = SystemProperties.get("user.home") + "/test";
	}

	@Test
	void test_createAndDeleteFile() throws IOException {
		Path testFile = Path.of(testPath, "upload", "test.txt");

		// 创建文件
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.create(testFile.getParent(), testFile));
		Assertions.assertThat(FileUtils.isExist(testFile)).isTrue();

		// 数据写入文件
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.write(testFile, "test content".getBytes(StandardCharsets.UTF_8)));
		Assertions.assertThat(Files.readString(testFile)).isEqualTo("test content");

		// 删除文件
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(testFile));
		Assertions.assertThat(FileUtils.isExist(testFile)).isFalse();

		// 创建文件
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.create(testFile.getParent().toString(), testFile.getFileName().toString()));
		Assertions.assertThat(FileUtils.isExist(testFile)).isTrue();

		// 删除文件
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.delete(testFile.getParent().toString(), testFile.getFileName().toString()));
		Assertions.assertThat(FileUtils.isExist(testFile)).isFalse();
		Assertions.assertThat(FileUtils.deleteIfExists(testFile)).isFalse();
	}

	@Test
	void test_checkPathExists() {
		Path existingFile = Path.of(testPath, "upload", "existing.txt");
		// 创建文件
		Assertions.assertThatNoException().isThrownBy(() -> {
			if (FileUtils.notExists(existingFile)) {
				FileUtils.createDirectories(existingFile.getParent());
				FileUtils.createFile(existingFile);
			}
		});
		Assertions.assertThat(FileUtils.isExist(existingFile)).isTrue();
		Assertions.assertThat(FileUtils.isExist(Path.of(testPath, "upload", "non_existent.txt"))).isFalse();
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(existingFile));
		Assertions.assertThat(FileUtils.notExists(existingFile)).isTrue();
	}

	@Test
	void test_fileStreamOperations() throws IOException {
		Path streamFile = Path.of(testPath, "upload", "stream.txt");
		byte[] content = "stream data".getBytes(StandardCharsets.UTF_8);
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.write(streamFile, content));
		byte[] readContent = FileUtils.getBytes(streamFile);
		Assertions.assertThat(readContent).isEqualTo(content);
		Assertions.assertThat(FileUtils.getStr(streamFile.toString())).isEqualTo("stream data");
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(streamFile));
		Assertions.assertThat(Files.notExists(streamFile)).isTrue();
	}

	@Test
	void test_zipOperations() throws IOException {
		// 测试多文件压缩场景
		Path srcFile1 = Path.of(testPath, "upload", "file1.txt");
		Path srcFile2 = Path.of(testPath, "upload", "file2.log");
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.create(srcFile1.getParent(), srcFile1.getFileName()));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.create(srcFile2.getParent(), srcFile2.getFileName()));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.writeString(srcFile1, "文件1内容"));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.writeString(srcFile2, "文件2日志内容"));

		// 创建多文件ZIP压缩包
		Path multiZip = Path.of(testPath, "zip", "multi.zip");
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.create(multiZip.getParent(), multiZip.getFileName()));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.zip(Path.of(testPath, "upload").toString(), multiZip.toString()));

		// 执行解压操作
		Path destDir = Path.of(testPath, "unzip");
		Assertions.assertThatNoException().isThrownBy(() -> {
			if (FileUtils.notExists(destDir)) {
				FileUtils.createDirectories(destDir);
			}
		});
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.unzip(multiZip, destDir));

		Assertions.assertThat(FileUtils.isExist(multiZip)).isTrue();
		try (BufferedReader reader = Files.newBufferedReader(srcFile1)) {
			Assertions.assertThat(reader.readLine()).isEqualTo("文件1内容");
		}
		try (BufferedReader reader = Files.newBufferedReader(srcFile2)) {
			Assertions.assertThat(reader.readLine()).isEqualTo("文件2日志内容");
		}
		Assertions.assertThat(FileUtils.isExist(Path.of(destDir.toString(), "upload", "file1.txt"))).isTrue();
		Assertions.assertThat(FileUtils.isExist(Path.of(destDir.toString(), "upload", "file2.log"))).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(multiZip)).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(srcFile1)).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(srcFile2)).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file1.txt"))).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file2.log"))).isTrue();
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(destDir.toString()));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(multiZip.toString()));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(Path.of(testPath, "upload").toString()));
		Assertions.assertThat(FileUtils.notExists(multiZip)).isTrue();
		Assertions.assertThat(FileUtils.notExists(destDir)).isTrue();
		Assertions.assertThat(FileUtils.notExists(Path.of(testPath, "upload"))).isTrue();
	}

}
