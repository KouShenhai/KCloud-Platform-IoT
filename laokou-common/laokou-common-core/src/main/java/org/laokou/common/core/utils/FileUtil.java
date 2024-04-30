/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.laokou.common.i18n.common.StringConstant.COMMA;
import static org.laokou.common.i18n.common.StringConstant.DOT;

/**
 * 文件工具类.
 *
 * @author laokou
 */
@Slf4j
public class FileUtil {

	/**
	 * 定义允许上传的文件扩展名.
	 */
	private static final Map<String, String> EXT_MAP = new HashMap<>(3);

	static {
		// 其中image,audio,video对应文件夹名称,对应dirName
		// key文件夹名称
		// value该文件夹内可以上传文件的后缀名
		EXT_MAP.put("image", ".gif,.GIF,.jpg,.JPG,.jpeg,.JPEG,.png,.PNG,.bmp,.BMP,.webp,.WEBP");
		EXT_MAP.put("audio",
				".flac,.FLAC,.cda,.wav,.mp3,.aif,.aiff,.mid,.wma,.ra,.vqf,.ape,.CDA,.WAV,.MP3,.AIF,.AIFF,.MID,.WMA,.RA,.VQF,.APE");
		EXT_MAP.put("video", ".mp4,.MP4,.AVI,.mov,.rmvb,.rm,.FLV,.mp4,.3GP,.flv");
	}

	/**
	 * 校验文件扩展名.
	 * @param type 类型
	 * @param fileExt 文件扩展名
	 * @return 校验结果
	 */
	public static boolean validateFileExt(String type, String fileExt) {
		String extValue = EXT_MAP.get(type);
		List<String> extList = Arrays.asList(extValue.split(COMMA));
		return extList.contains(fileExt);
	}

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
		try (ZipOutputStream zos = new ZipOutputStream(os); FileInputStream fis = new FileInputStream(file)) {
			zos.putNextEntry(new ZipEntry(file.getName()));
			zos.write(fis.readAllBytes());
		}
	}

}
