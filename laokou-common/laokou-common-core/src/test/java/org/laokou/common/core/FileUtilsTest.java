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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

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
		assertThatNoException().isThrownBy(() -> FileUtils.create(testFile.getParent(), testFile));
		assertThat(FileUtils.isExist(testFile)).isTrue();

		// 数据写入文件
		assertThatNoException()
			.isThrownBy(() -> FileUtils.write(testFile, "test content".getBytes(StandardCharsets.UTF_8)));
		assertThat(Files.readString(testFile)).isEqualTo("test content");

		// 删除文件
		assertThatNoException().isThrownBy(() -> FileUtils.delete(testFile));
		assertThat(FileUtils.isExist(testFile)).isFalse();

		// 创建文件
		assertThatNoException()
			.isThrownBy(() -> FileUtils.create(testFile.getParent().toString(), testFile.getFileName().toString()));
		assertThat(FileUtils.isExist(testFile)).isTrue();

		// 删除文件
		assertThatNoException()
			.isThrownBy(() -> FileUtils.delete(testFile.getParent().toString(), testFile.getFileName().toString()));
		assertThat(FileUtils.isExist(testFile)).isFalse();
		assertThat(FileUtils.deleteIfExists(testFile)).isFalse();
	}

	@Test
	void testCheckPathExists() {
		Path existingFile = Path.of(testPath, "upload", "existing.txt");
		// 创建文件
		assertThatNoException().isThrownBy(() -> {
			if (FileUtils.notExists(existingFile)) {
				FileUtils.createDirectories(existingFile.getParent());
				FileUtils.createFile(existingFile);
			}
		});
		assertThat(FileUtils.isExist(existingFile)).isTrue();
		assertThat(FileUtils.isExist(Path.of(testPath, "upload", "non_existent.txt"))).isFalse();
		assertThatNoException().isThrownBy(() -> FileUtils.delete(existingFile));
		assertThat(FileUtils.notExists(existingFile)).isTrue();
	}

	@Test
	void testFileStreamOperations() throws IOException {
		Path streamFile = Path.of(testPath, "upload", "stream.txt");
		byte[] content = "stream data".getBytes(StandardCharsets.UTF_8);
		assertThatNoException().isThrownBy(() -> FileUtils.write(streamFile, content));
		byte[] readContent = FileUtils.getBytes(streamFile);
		assertThat(readContent).isEqualTo(content);
		assertThat(FileUtils.getStr(streamFile.toString())).isEqualTo("stream data");
		assertThatNoException().isThrownBy(() -> FileUtils.delete(streamFile));
		assertThat(Files.notExists(streamFile)).isTrue();
	}

	@Test
	void testZipOperations() throws IOException {
		// 测试多文件压缩场景
		Path srcFile1 = Path.of(testPath, "upload", "file1.txt");
		Path srcFile2 = Path.of(testPath, "upload", "file2.log");
		assertThatNoException().isThrownBy(() -> FileUtils.create(srcFile1.getParent(), srcFile1.getFileName()));
		assertThatNoException().isThrownBy(() -> FileUtils.create(srcFile2.getParent(), srcFile2.getFileName()));
		assertThatNoException().isThrownBy(() -> FileUtils.writeString(srcFile1, "文件1内容"));
		assertThatNoException().isThrownBy(() -> FileUtils.writeString(srcFile2, "文件2日志内容"));

		// 创建多文件ZIP压缩包
		Path multiZip = Path.of(testPath, "zip", "multi.zip");
		assertThatNoException().isThrownBy(() -> FileUtils.create(multiZip.getParent(), multiZip.getFileName()));
		assertThatNoException()
			.isThrownBy(() -> FileUtils.zip(Path.of(testPath, "upload").toString(), multiZip.toString()));

		// 执行解压操作
		Path destDir = Path.of(testPath, "unzip");
		assertThatNoException().isThrownBy(() -> {
			if (FileUtils.notExists(destDir)) {
				FileUtils.createDirectories(destDir);
			}
		});
		assertThatNoException().isThrownBy(() -> FileUtils.unzip(multiZip, destDir));

		assertThat(FileUtils.isExist(multiZip)).isTrue();
		try (BufferedReader reader = Files.newBufferedReader(srcFile1)) {
			assertThat(reader.readLine()).isEqualTo("文件1内容");
		}
		try (BufferedReader reader = Files.newBufferedReader(srcFile2)) {
			assertThat(reader.readLine()).isEqualTo("文件2日志内容");
		}
		assertThat(FileUtils.isExist(Path.of(destDir.toString(), "upload", "file1.txt"))).isTrue();
		assertThat(FileUtils.isExist(Path.of(destDir.toString(), "upload", "file2.log"))).isTrue();
		assertThat(FileUtils.deleteIfExists(multiZip)).isTrue();
		assertThat(FileUtils.deleteIfExists(srcFile1)).isTrue();
		assertThat(FileUtils.deleteIfExists(srcFile2)).isTrue();
		assertThat(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file1.txt"))).isTrue();
		assertThat(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file2.log"))).isTrue();
		assertThatNoException().isThrownBy(() -> FileUtils.delete(destDir.toString()));
		assertThatNoException().isThrownBy(() -> FileUtils.delete(multiZip.toString()));
		assertThatNoException().isThrownBy(() -> FileUtils.delete(Path.of(testPath, "upload").toString()));
		assertThat(FileUtils.notExists(multiZip)).isTrue();
		assertThat(FileUtils.notExists(destDir)).isTrue();
		assertThat(FileUtils.notExists(Path.of(testPath, "upload"))).isTrue();
	}

}
