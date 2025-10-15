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
import org.laokou.common.core.util.SystemUtils;
import org.laokou.common.i18n.util.SslUtils;
import org.springframework.boot.system.SystemProperties;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.FileAttribute;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

class FileUtilsTest {

	private String testPath;

	@BeforeEach
	void setUp() {
		testPath = SystemProperties.get("user.home") + "/test";
	}

	@Test
	void test_baseApi() throws IOException, NoSuchAlgorithmException, KeyManagementException {
		SslUtils.ignoreSSLTrust();
		Assertions.assertThat(FileUtils.getFileExt("test.png")).isEqualTo(".png");
		Assertions.assertThat(FileUtils.getBytesByUrl(
				"https://i11.hoopchina.com.cn/hupuapp/bbs/0/0/thread_0_20190620145828_s_64509_o_w_387_h_600_20991.png"))
			.isNotEmpty();
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.walkFileTree(Path.of(testPath), new SimpleFileVisitor<>() {
			}));
		Path testFilePath = Path.of(testPath, "upload22", "test.txt");
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.createDirAndFile(testFilePath));
		Assertions.assertThat(FileUtils.exists(testFilePath)).isTrue();
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.write(testFilePath, "dsdfsd,343,erew".getBytes()));
		try (InputStream inputStream = FileUtils.newInputStream(testFilePath)) {
			Assertions.assertThat(inputStream).isNotNull();
			Assertions.assertThat(inputStream.readAllBytes()).isEqualTo("dsdfsd,343,erew".getBytes());
		}
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.replaceFirstFromEnd(testFilePath.toString(), ',', '!'));
		try (InputStream inputStream = FileUtils.newInputStream(testFilePath)) {
			Assertions.assertThat(inputStream).isNotNull();
			Assertions.assertThat(inputStream.readAllBytes()).isEqualTo("dsdfsd,343!erew".getBytes());
		}
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.write(testFilePath, "dsdfsd,343,erew".getBytes()));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.replaceAllFromEnd(testFilePath.toString(), ',', '!'));
		try (InputStream inputStream = FileUtils.newInputStream(testFilePath)) {
			Assertions.assertThat(inputStream).isNotNull();
			Assertions.assertThat(inputStream.readAllBytes()).isEqualTo("dsdfsd!343!erew".getBytes());
		}
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.deleteIfExists(testFilePath));
		Assertions.assertThat(FileUtils.notExists(testFilePath)).isTrue();
	}

	@Test
	void test_createAndDeleteFile() throws IOException {
		Path testFilePath = Path.of(testPath, "upload", "test.txt");

		// 创建文件
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.createDirAndFile(testFilePath));
		Assertions.assertThat(FileUtils.exists(testFilePath)).isTrue();

		// 数据写入文件
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.write(testFilePath, "test content".getBytes(StandardCharsets.UTF_8)));
		Assertions.assertThat(FileUtils.readString(testFilePath)).isEqualTo("test content");
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.write(Path.of(testPath, "upload", "test2.txt_a"),
					new ByteArrayInputStream("a".getBytes()), "a".getBytes().length));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.write(Path.of(testPath, "upload", "test2.txt_b"),
					new ByteArrayInputStream("b".getBytes()), "b".getBytes().length));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.write(Path.of(testPath, "upload", "test2.txt_c"),
					new ByteArrayInputStream("c".getBytes()), "c".getBytes().length));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.chunkWrite(Path.of(testPath, "upload", "test2.txt").toFile(),
					List.of(new FileUtils.Chunk(Path.of(testPath, "upload", "test2.txt_a").toFile(), 0, 1),
							new FileUtils.Chunk(Path.of(testPath, "upload", "test2.txt_b").toFile(), 1, 1),
							new FileUtils.Chunk(Path.of(testPath, "upload", "test2.txt_c").toFile(), 2, 1))));
		Assertions.assertThat(FileUtils.exists(Path.of(testPath, "upload", "test2.txt"))).isTrue();
		Assertions.assertThat(FileUtils.readString((Path.of(testPath, "upload", "test2.txt")))).isEqualTo("abc");

		Assertions.assertThatNoException().isThrownBy(() -> {
			Path path;
			Path path2;
			if (SystemUtils.isWindows()) {
				path = FileUtils.createTempFile(Path.of(testPath), "12222", "txt");
				path2 = FileUtils.createTempDirectory(Path.of(testPath), "test1231");
			}
			else {
				path = FileUtils.createTempFile(Path.of(testPath), "12222", "txt", new FileAttribute[] {});
				path2 = FileUtils.createTempDirectory(Path.of(testPath), "test1231", new FileAttribute[] {});
			}
			Assertions.assertThat(FileUtils.exists(path)).isTrue();
			Assertions.assertThat(FileUtils.exists(path2)).isTrue();
			Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(path));
			Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(path2));
			Assertions.assertThat(FileUtils.notExists(path)).isTrue();
			Assertions.assertThat(FileUtils.notExists(path2)).isTrue();
		});

		// 删除文件
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(testFilePath));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(Path.of(testPath, "upload", "test2.txt")));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.delete(Path.of(testPath, "upload", "test2.txt_a")));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.delete(Path.of(testPath, "upload", "test2.txt_b")));
		Assertions.assertThatNoException()
			.isThrownBy(() -> FileUtils.delete(Path.of(testPath, "upload", "test2.txt_c")));
		Assertions.assertThat(FileUtils.exists(testFilePath)).isFalse();
		Assertions.assertThat(FileUtils.exists(Path.of(testPath, "upload", "test2.txt"))).isFalse();
		Assertions.assertThat(FileUtils.exists(Path.of(testPath, "upload", "test2.txt_a"))).isFalse();
		Assertions.assertThat(FileUtils.exists(Path.of(testPath, "upload", "test2.txt_b"))).isFalse();
		Assertions.assertThat(FileUtils.exists(Path.of(testPath, "upload", "test2.txt_c"))).isFalse();

		// 创建文件
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.createDirAndFile(testFilePath));
		Assertions.assertThat(FileUtils.exists(testFilePath)).isTrue();

		// 删除文件
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(testFilePath));
		Assertions.assertThat(FileUtils.exists(testFilePath)).isFalse();
		Assertions.assertThat(FileUtils.deleteIfExists(testFilePath)).isFalse();
	}

	@Test
	void test_checkPathExists() {
		Path existingFile = Path.of(testPath, "upload", "existing.txt");
		// 创建文件
		Assertions.assertThatNoException().isThrownBy(() -> {
			if (FileUtils.notExists(existingFile)) {
				FileUtils.createDirAndFile(existingFile);
			}
		});
		Assertions.assertThat(FileUtils.exists(existingFile)).isTrue();
		Assertions.assertThat(FileUtils.exists(Path.of(testPath, "upload", "non_existent.txt"))).isFalse();
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
		Assertions.assertThat(FileUtils.readString(streamFile)).isEqualTo("stream data");
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(streamFile));
		Assertions.assertThat(FileUtils.notExists(streamFile)).isTrue();
	}

	@Test
	void test_zipOperations() throws IOException {
		// 测试多文件压缩场景
		Path srcFile1 = Path.of(testPath, "upload", "file1.txt");
		Path srcFile2 = Path.of(testPath, "upload", "file2.log");
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.createDirAndFile(srcFile1));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.createDirAndFile(srcFile2));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.writeString(srcFile1, "文件1内容"));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.writeString(srcFile2, "文件2日志内容"));

		// 创建多文件ZIP压缩包
		Path multiZip = Path.of(testPath, "zip", "multi.zip");
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.createDirAndFile(multiZip));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.zip(Path.of(testPath, "upload"), multiZip));
		try (OutputStream outputStream = FileUtils.newOutputStream(multiZip)) {
			Assertions.assertThatNoException()
				.isThrownBy(() -> FileUtils.zip(Path.of(testPath, "upload"), outputStream));
		}

		// 执行解压操作
		Path destDir = Path.of(testPath, "unzip");
		Assertions.assertThatNoException().isThrownBy(() -> {
			if (FileUtils.notExists(destDir)) {
				FileUtils.createDirectories(destDir);
			}
		});
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.unzip(multiZip, destDir));

		Assertions.assertThat(FileUtils.exists(multiZip)).isTrue();

		try (InputStream inputStream = FileUtils.newInputStream(srcFile1)) {
			Assertions.assertThat(inputStream.readAllBytes()).isEqualTo("文件1内容".getBytes());
		}
		try (BufferedReader reader = FileUtils.newBufferedReader(srcFile1)) {
			Assertions.assertThat(reader.readLine()).isEqualTo("文件1内容");
		}
		try (BufferedReader reader = FileUtils.newBufferedReader(srcFile2)) {
			Assertions.assertThat(reader.readLine()).isEqualTo("文件2日志内容");
		}
		Assertions.assertThat(FileUtils.exists(Path.of(destDir.toString(), "upload", "file1.txt"))).isTrue();
		Assertions.assertThat(FileUtils.exists(Path.of(destDir.toString(), "upload", "file2.log"))).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(multiZip)).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(srcFile1)).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(srcFile2)).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file1.txt"))).isTrue();
		Assertions.assertThat(FileUtils.deleteIfExists(Path.of(destDir.toString(), "upload", "file2.log"))).isTrue();
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(destDir));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(multiZip));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(Path.of(testPath, "zip")));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(Path.of(testPath, "unzip")));
		Assertions.assertThatNoException().isThrownBy(() -> FileUtils.delete(Path.of(testPath, "upload")));
		Assertions.assertThat(FileUtils.notExists(multiZip)).isTrue();
		Assertions.assertThat(FileUtils.notExists(destDir)).isTrue();
		Assertions.assertThat(FileUtils.notExists(Path.of(testPath, "upload"))).isTrue();
		Assertions.assertThat(FileUtils.notExists(Path.of(testPath, "zip"))).isTrue();
		Assertions.assertThat(FileUtils.notExists(Path.of(testPath, "unzip"))).isTrue();
	}

}
