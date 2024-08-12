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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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

	/**
	 * 创建目录及文件.
	 * @param directory 目录
	 * @param fileName 文件名
	 * @return 创建后的文件对象
	 */
	@SneakyThrows
	public static File createFile(String directory, String fileName) {
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

	/**
	 * 获取文件扩展名.
	 * @param fileName 文件名称
	 * @return 文件扩展名
	 */
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf(DOT));
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			log.info("删除文件：{}", file.delete());
		}
	}

	@SneakyThrows
	public static void deleteFile(String path) {
		Files.walkFileTree(Path.of(path), new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				deleteFile(path.toFile());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * zip压缩包.
	 * @param source 源
	 * @param target 目标
	 */
	@SneakyThrows
	private static void zip(String source, String target) {
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target))) {
			Path sourceDir = Path.of(source);
			Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
				@Override
				@SneakyThrows
				public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
					// 对于每个文件，创建一个 ZipEntry 并写入
					Path targetPath = sourceDir.relativize(path);
					zos.putNextEntry(new ZipEntry(sourceDir.getFileName() + SLASH + targetPath));
					Files.copy(path, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				@Override
				@SneakyThrows
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
					// 对于每个目录，创建一个 ZipEntry（目录也需要在 ZIP 中存在）
					Path targetPath = sourceDir.relativize(path);
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
