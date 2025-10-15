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

package org.laokou.tool;

import org.laokou.common.core.util.FileUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

/**
 * @author laokou
 */
final class ErrorInfoPrinter {

	/**
	 * 错误等级.
	 */
	private static final String ERROR = "ERROR";

	private ErrorInfoPrinter() {

	}

	// @formatter:off
	public static void main(String[] args) throws IOException {
		// 应用名称
		String appName = "generator";
		// 服务ID
		String serviceId = "laokou-generator";
		// JSON文件名称
		String jsonName = String.format("%s.json", serviceId);
		// 项目目录路径
		String projectPath = System.getProperty("user.dir");
		// 日志目录路径
		String logPath = projectPath + StringConstants.SLASH + "logs";
		// JSON文件路径
		String jsonPath = logPath + StringConstants.SLASH + appName + StringConstants.SLASH + jsonName;
		// 临时文件路径
		String tempPath = projectPath + StringConstants.SLASH + "temp";
		// 创建临时文件
		Path pt = Path.of(tempPath, jsonName);
		Path path = FileUtils.createDirAndFile(pt);
		FileUtils.write(path, FileUtils.getBytes(Path.of(jsonPath)));
		// 替换字符【从后往前】
		File file = path.toFile();
		FileUtils.replaceFirstFromEnd(file.getAbsolutePath(), ',', ']');
		// 读取JSON文件
		List<TraceLog> list = JacksonUtils.toList(file, TraceLog.class);
		StringBuilder sb = new StringBuilder();
		for (TraceLog traceLog : list) {
			if (ObjectUtils.equals(traceLog.getLevel(), ERROR)
				&& StringExtUtils.isNotEmpty(traceLog.getStacktrace())) {
				sb.append(traceLog.getStacktrace());
			}
		}
		// 临时文件名称
		String tempName = "stacktrace.txt";
		// 写入文件【堆栈信息】
		FileUtils.write(Path.of(tempPath, tempName), sb.toString().getBytes(StandardCharsets.UTF_8));
		// 删除临时文件
		FileUtils.delete(pt);
	}
	// @formatter:on

}
