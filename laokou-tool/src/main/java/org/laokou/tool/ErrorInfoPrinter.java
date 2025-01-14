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

import org.laokou.common.core.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.laokou.common.i18n.common.constant.StringConstant.SLASH;

/**
 * @author laokou
 */
final class ErrorInfoPrinter {

	private ErrorInfoPrinter() {

	}

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
		String logPath = projectPath + SLASH + "logs";
		// JSON文件路径
		String jsonPath = logPath + SLASH + appName + SLASH + jsonName;
		// 临时文件路径
		String tempPath = projectPath + SLASH + "temp";
		// 创建临时文件
		FileUtil.create(tempPath, jsonName);
		// 写入内容到临时文件
		File file = new File(tempPath, jsonName);
		FileUtil.write(file, FileUtil.getBytes(Path.of(jsonPath)));
	}

}
