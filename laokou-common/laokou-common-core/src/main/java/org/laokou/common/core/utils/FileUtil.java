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

package org.laokou.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.StringUtil;
import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.laokou.common.i18n.common.constant.StringConstant.*;

/**
 * 文件工具类.
 *
 * @author laokou
 */
@Slf4j
public final class FileUtil {

	private static final String RW = "rw";

	private FileUtil() {
	}

	public static InputStream newInputStream(String path) throws IOException {
		return Files.newInputStream(Path.of(path));
	}

	public static BufferedReader newBufferedReader(String path) throws IOException {
		return Files.newBufferedReader(Path.of(path));
	}

	/**
	 * 创建目录及文件.
	 * @param directory 目录
	 * @param fileName 文件名
	 * @return 创建后的文件对象
	 */
	public static Path create(String directory, String fileName) throws IOException {
		Path directoryPath = Path.of(directory);
		Path filePath = Path.of(directory, fileName);
		return create(directoryPath, filePath);
	}

	/**
	 * 创建目录及文件.
	 * @param directoryPath 目录
	 * @param filePath 文件名
	 * @return 创建后的文件对象
	 */
	public static Path create(Path directoryPath, Path filePath) throws IOException {
		if (!isExist(directoryPath)) {
			Files.createDirectories(directoryPath);
		}
		if (!isExist(filePath)) {
			Files.createFile(filePath);
		}
		return filePath;
	}

	public static byte[] getBytes(String url) throws IOException {
		URI uri = URI.create(url);
		URLConnection connection = uri.toURL().openConnection();
		try (InputStream in = connection.getInputStream()) {
			return in.readAllBytes();
		}
	}

	public static byte[] getBytes(Path path) throws IOException {
		return Files.readAllBytes(path);
	}

	public static String getStr(String path) throws IOException {
		return getStr(Path.of(path));
	}

	public static void walkFileTree(Path path, FileVisitor<? super Path> visitor) throws IOException {
		Files.walkFileTree(path, visitor);
	}

	public static void write(Path path, byte[] buff) throws IOException {
		Files.write(path, buff);
	}

	public static void write(Path path, InputStream inputStream, long size) throws NoSuchAlgorithmException {
		try (FileOutputStream fos = new FileOutputStream(path.toFile());
				FileChannel outChannel = fos.getChannel();
				ReadableByteChannel inChannel = Channels.newChannel(inputStream);) {
			outChannel.transferFrom(inChannel, 0, size);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void chunkWrite(File file, InputStream in, long start, long end, long size) {
		if (in instanceof FileInputStream fis) {
			try (FileChannel inChannel = fis.getChannel()) {
				chunkWrite(file, inChannel, start, end, size);
			}
			catch (IOException e) {
				log.error("错误信息：{}", e.getMessage());
				throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
			}
		}
	}

	public static void write(File file, InputStream in, long size, long chunkSize,
			ExecutorService virtualThreadExecutor) throws IOException {
		if (in instanceof FileInputStream fis) {
			// 最大偏移量2G【2^31】数据
			assert chunkSize > 0L;
			chunkSize = Math.min(chunkSize, 2L * 1024 * 1024 * 1024);
			long chunkCount = (size / chunkSize) + (size % chunkSize == 0 ? 0 : 1);
			try (FileChannel inChannel = fis.getChannel()) {
				List<Callable<Boolean>> futures = new ArrayList<>((int) chunkCount);
				// start指针【position偏移量】
				for (long index = 0, start = 0,
						end = start + chunkSize; index < chunkCount; index++, start = index * chunkSize) {
					long finalStart = start;
					futures.add(() -> {
						chunkWrite(file, inChannel, finalStart, end, size);
						return true;
					});
				}
				virtualThreadExecutor.invokeAll(futures);
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("未知错误，错误信息：{}", e.getMessage(), e);
				throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
			}
		}
	}

	/**
	 * 获取文件扩展名.
	 * @param fileName 文件名称
	 * @return 文件扩展名
	 */
	public static String getFileExt(String fileName) {
		if (StringUtil.isEmpty(fileName)) {
			return EMPTY;
		}
		return fileName.substring(fileName.lastIndexOf(DOT));
	}

	public static void copy(Path source, OutputStream out) throws IOException {
		Files.copy(source, out);
	}

	public static void delete(Path path) throws IOException {
		Files.delete(path);
	}

	public static void delete(String directory, String fileName) throws IOException {
		Path path = Path.of(directory, fileName);
		if (isExist(path)) {
			delete(path);
		}
	}

	public static boolean isExist(Path path) {
		return Files.exists(path);
	}

	public static void delete(String directory) throws IOException {
		Path path = Path.of(directory);
		if (isExist(path)) {
			walkFileTree(path, new SimpleFileVisitor<>() {

				@Override
				public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
					delete(filePath);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dirPath, IOException exc) throws IOException {
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

	public static void zip(String sourcePath, String targetPath) throws IOException {
		zip(sourcePath, Files.newOutputStream(Path.of(targetPath)));
	}

	/**
	 * zip压缩包.
	 * @param sourcePath 源路径
	 * @param out 输出流
	 */
	public static void zip(String sourcePath, OutputStream out) throws IOException {
		try (ZipOutputStream zos = new ZipOutputStream(out)) {
			Path sourceDir = Path.of(sourcePath);
			walkFileTree(sourceDir, new SimpleFileVisitor<>() {

				@Override
				public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
					// 对于每个文件，创建一个 ZipEntry 并写入
					Path targetPath = sourceDir.relativize(filePath);
					zos.putNextEntry(new ZipEntry(sourceDir.getFileName() + SLASH + targetPath));
					copy(filePath, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dirPath, BasicFileAttributes attrs) throws IOException {
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

	public static void replaceFirstFromEnd(String sourcePath, char oldChar, char newChar) {
		replaceFromEnd(sourcePath, oldChar, newChar, true);
	}

	public static void replaceAllFromEnd(String sourcePath, char oldChar, char newChar) {
		replaceFromEnd(sourcePath, oldChar, newChar, false);
	}

	private static void replaceFromEnd(String sourcePath, char oldChar, char newChar, boolean stopAfterFirst) {
		try (RandomAccessFile raf = new RandomAccessFile(sourcePath, RW)) {
			for (long pos = raf.length() - 1; pos >= 0; pos--) {
				raf.seek(pos);
				if ((char) raf.read() == oldChar) {
					raf.seek(pos);
					raf.write(newChar);
					if (stopAfterFirst) {
						break;
					}
				}
			}
		}
		catch (IOException e) {
			log.error("错误信息：{}", e.getMessage());
			throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
		}
	}

	private static void chunkWrite(File file, FileChannel inChannel, long start, long end, long size) {
		try (RandomAccessFile accessFile = new RandomAccessFile(file, RW);
				FileChannel outChannel = accessFile.getChannel()) {
			// 零拷贝
			// transferFrom 与 transferTo 区别
			// transferTo 最多拷贝2gb，和源文件大小保持一致【发送，从当前通道读取数据并写入外部通道】
			// transferFrom【接收，从外部通道读取数据并写入当前通道】
			outChannel.position(start);
			inChannel.transferTo(start, Math.min(end, size), outChannel);
		}
		catch (IOException e) {
			log.error("错误信息：{}", e.getMessage());
			throw new SystemException("S_UnKnow_Error", e.getMessage(), e);
		}
	}

	private static String getStr(Path path) throws IOException {
		return Files.readString(path, StandardCharsets.UTF_8);
	}

}
