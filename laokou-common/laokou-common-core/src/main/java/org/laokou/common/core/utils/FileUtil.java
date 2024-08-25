/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.core.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.laokou.common.i18n.common.constant.StringConstant.DOT;
import static org.laokou.common.i18n.common.constant.StringConstant.SLASH;

/**
 * 文件工具类.
 *
 * @author laokou
 */
@Slf4j
public class FileUtil {

	private static final String RW = "rw";

	/**
	 * 创建目录及文件.
	 *
	 * @param directory 目录
	 * @param fileName  文件名
	 * @return 创建后的文件对象
	 */
	@SneakyThrows
	public static File create(String directory, String fileName) {
		File directoryFile = new File(directory);
		if (!directoryFile.exists()) {
			log.info("目录创建：{}", directoryFile.mkdirs());
		}
		File newFile = new File(directoryFile, fileName);
		if (!newFile.exists()) {
			log.info("文件创建：{}", newFile.createNewFile());
		}
		return newFile;
	}

	public static byte[] getBytes(Path path) throws IOException {
		return Files.readAllBytes(path);
	}

	public static String getStr(Path path) throws IOException {
		return Files.readString(path, StandardCharsets.UTF_8);
	}

	@SneakyThrows
	public static Path walkFileTree(Path path, FileVisitor<? super Path> visitor) {
		return Files.walkFileTree(path, visitor);
	}

	@SneakyThrows
	public static void write(Path path, byte[] buff) {
		Files.write(path, buff);
	}

	@SneakyThrows
	public static void write(File file, byte[] buff) {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(buff);
		}
	}

	public static void write(File file, InputStream in, long size, long chunkSize, Executor executor)
		throws IOException {
		if (in instanceof FileInputStream fis) {
			try (FileChannel inChannel = fis.getChannel()) {
				long chunkCount = (size / chunkSize) + (size % chunkSize == 0 ? 0 : 1);
				List<CompletableFuture<Void>> futures = new ArrayList<>((int) chunkCount);
				// position指针
				for (long index = 0, position = 0,
					 endSize = position + chunkSize; index < chunkCount; index++, position = index * chunkSize) {
					long finalPosition = position;
					futures.add(CompletableFuture.runAsync(() -> {
						try (RandomAccessFile accessFile = new RandomAccessFile(file, RW);
							 FileChannel outChannel = accessFile.getChannel()) {
							// 结束位置
							long finalEndSize = endSize;
							if (finalEndSize > size) {
								finalEndSize = size;
							}
							outChannel.position(finalPosition);
							// 零拷贝
							// transferFrom 与 transferTo 区别
							// transferTo 最多拷贝2gb，和源文件大小保持一致【发送，从当前通道读取数据并写入外部通道】
							// transferFrom【接收，从外部通道读取数据并写入当前通道】
							inChannel.transferTo(finalPosition, finalEndSize, outChannel);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}, executor));
				}
				futures.forEach(CompletableFuture::join);
			}
		}
	}

	/**
	 * 获取文件扩展名.
	 *
	 * @param fileName 文件名称
	 * @return 文件扩展名
	 */
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf(DOT));
	}

	@SneakyThrows
	public static void copy(Path source, OutputStream out) {
		Files.copy(source, out);
	}

	@SneakyThrows
	public static void delete(Path path) {
		Files.delete(path);
	}

	public static boolean isExist(Path path) {
		return Files.exists(path);
	}

	@SneakyThrows
	public static void delete(String directory) {
		Path path = Path.of(directory);
		if (isExist(path)) {
			walkFileTree(path, new SimpleFileVisitor<>() {

				@Override
				public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {
					delete(filePath);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dirPath, IOException exc) {
					delete(dirPath);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

	@SneakyThrows
	public static void zip(String sourcePath, String targetPath) {
		zip(sourcePath, new FileOutputStream(targetPath));
	}

	/**
	 * zip压缩包.
	 *
	 * @param sourcePath 源路径
	 * @param out        输出流
	 */
	@SneakyThrows
	public static void zip(String sourcePath, OutputStream out) {
		try (ZipOutputStream zos = new ZipOutputStream(out)) {
			Path sourceDir = Path.of(sourcePath);
			walkFileTree(sourceDir, new SimpleFileVisitor<>() {
				@Override
				@SneakyThrows
				public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {
					// 对于每个文件，创建一个 ZipEntry 并写入
					Path targetPath = sourceDir.relativize(filePath);
					zos.putNextEntry(new ZipEntry(sourceDir.getFileName() + SLASH + targetPath));
					copy(filePath, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				@Override
				@SneakyThrows
				public FileVisitResult preVisitDirectory(Path dirPath, BasicFileAttributes attrs) {
					// 对于每个目录，创建一个 ZipEntry（目录也需要在 ZIP 中存在）
					Path targetPath = sourceDir.relativize(dirPath);
					zos.putNextEntry(new ZipEntry(sourceDir.getFileName() + SLASH + targetPath + SLASH));
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

}
