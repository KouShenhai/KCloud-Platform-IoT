package org.laokou.common.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.laokou.common.core.util.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

	@TempDir
	private Path tempDir;

	@Test
	void testCreateAndDeleteFile() throws IOException {
		Path testFile = tempDir.resolve("test.txt");

		// 创建文件
		FileUtils.create(testFile.getParent(), testFile);
		assertTrue(FileUtils.isExist(testFile));

		// 数据写入文件
		FileUtils.write(testFile, "test content".getBytes(StandardCharsets.UTF_8));
		assertEquals("test content", Files.readString(testFile));

		// 删除文件
		FileUtils.delete(testFile);
		assertFalse(FileUtils.isExist(testFile));

		// 创建文件
		FileUtils.create(testFile.getParent().toString(), testFile.getFileName().toString());
		assertTrue(FileUtils.isExist(testFile));

		// 删除文件
		FileUtils.delete(testFile.getParent().toString(), testFile.getFileName().toString());
		assertFalse(FileUtils.isExist(testFile));
		assertFalse(FileUtils.deleteIfExists(testFile));
	}

	@Test
	void testCheckPathExists() {
		Path existingFile = tempDir.resolve("existing.txt");
		// 创建文件
		assertDoesNotThrow(() -> {
			FileUtils.createDirectories(existingFile.getParent());
			FileUtils.createFile(existingFile);
		});
		assertTrue(FileUtils.isExist(existingFile));
		assertFalse(FileUtils.isExist(tempDir.resolve("non_existent.txt")));
	}

	@Test
	void testFileStreamOperations() throws IOException {
		Path streamFile = tempDir.resolve("stream.txt");
		byte[] content = "stream data".getBytes(StandardCharsets.UTF_8);
		FileUtils.write(streamFile, content);
		byte[] readContent = FileUtils.getBytes(streamFile);
		assertArrayEquals(content, readContent);
		assertEquals("stream data", FileUtils.getStr(streamFile.toString()));
	}

	@Test
	void testZipOperations() throws IOException {
		// 测试多文件压缩场景
		Path srcFile1 = tempDir.resolve("file1.txt");
		Path srcFile2 = tempDir.resolve("file2.log");
		FileUtils.writeString(srcFile1, "文件1内容");
		FileUtils.writeString(srcFile2, "文件2日志内容");

		// 创建多文件ZIP压缩包
		Path multiZip = tempDir.resolve("multi.zip");
		FileUtils.zipFile(srcFile1.toString(), multiZip.toString());
		FileUtils.zipFile(srcFile2.toString(), multiZip.toString());

		// 测试单文件ZIP压缩包
		Path singleZip = tempDir.resolve("single.zip");
		FileUtils.zipFile(srcFile1.toString(), singleZip.toString());

		// 执行解压操作
		Path destDir = tempDir.resolve("unzip");
		Files.createDirectories(destDir);
		FileUtils.unzip(multiZip, destDir);

		// 验证所有断言
		assertAll("ZIP操作综合测试", () -> assertTrue(FileUtils.isExist(multiZip), "多文件ZIP校验失败"),
				() -> assertTrue(FileUtils.isExist(singleZip), "单文件ZIP校验失败"));
	}

}
