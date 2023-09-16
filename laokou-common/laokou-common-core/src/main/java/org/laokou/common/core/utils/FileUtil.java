/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import org.laokou.common.i18n.common.Constant;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import static org.laokou.common.i18n.common.Constant.COMMA;

/**
 * @author laokou
 */
@Slf4j
public class FileUtil {

	/**
	 * 定义允许上传的文件扩展名
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

	public static Boolean checkFileExt(String code, String fileExt) {
		String extValue = EXT_MAP.get(code);
		List<String> extList = Arrays.asList(extValue.split(COMMA));
		return extList.contains(fileExt);
	}

	public static void download(String directory, String fileName, InputStream in, long fileSize, long chunkSize) {
	}

	@SneakyThrows
	private static File createFile(String directory, String fileName) {
		File directoryFile = new File(directory);
		if (!directoryFile.exists()) {
			directoryFile.mkdirs();
		}
		File newFile = new File(directoryFile, fileName);
		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		return newFile;
	}

	/**
	 * 获取文件后缀
	 * @param fileName
	 */
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf(Constant.DOT));
	}

}
