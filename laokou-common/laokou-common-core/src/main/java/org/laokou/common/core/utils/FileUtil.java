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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.laokou.common.i18n.common.constant.StringConstant.DOT;

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

	/**
	 * 下载zip压缩包.
	 * @param file 文件
	 * @param os 输出流
	 * @throws IOException 异常
	 */
	public static void zip(File file, OutputStream os) throws IOException {
		// @formatter:off
		try (ZipOutputStream zos = new ZipOutputStream(os);
			 FileInputStream fis = new FileInputStream(file)) {
		// @formatter:on
			zos.putNextEntry(new ZipEntry(file.getName()));
			zos.write(fis.readAllBytes());
		}
	}

}
