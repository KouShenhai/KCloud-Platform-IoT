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

package org.laokou.tool;

import org.laokou.common.core.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static org.laokou.common.core.utils.SystemUtil.isWindows;

/**
 * 一键修改项目.
 *
 * @author laokou
 */
public class ModifyProjectBoot {

	private static final List<String> MODULES = List.of("laokou-cloud", "laokou-common", "laokou-service",
		"laokou-cola", "laokou-tool", "checkstyle");

	private static final String MODIFY_POM_FILE_SUFFIX = "pom.xml";

	private static final String MODIFY_JAVA_FILE_SUFFIX = ".java";

	private static final String MODIFY_XML_FILE_SUFFIX = ".xml";

	// 新模块名称
	private static final String NEW_MODULE_NAME = "newlaokou";

	// 新分组ID
	private static final String NEW_GROUP_ID = "cn.org.laokou";

	// 新包名路径[Linux]
	private static final String NEW_PACKAGE_PATH_LINUX = "/cn/org/laokou/";

	// 新包名路径[Window]
	private static final String NEW_PACKAGE_PATH_WINDOW = "\\\\cn\\\\org\\\\laokou\\\\";

	// 新包名名称
	private static final String NEW_PACKAGE_NAME = "cn.org.laokou";

	// 新项目名称
	private static final String NEW_PROJECT_NAME = "New-KCloud-Platform-IoT";

	private static int count = 0;

	public static void main(String[] args) throws IOException {

		// 请在根目录移除maven-checkstyle-plugin
		// 请在根目录移除maven-checkstyle-plugin
		// 请在根目录移除maven-checkstyle-plugin

		// 修改projectName、packageName、groupId、artifactId
		String projectPath = System.getProperty("user.dir");
		Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<>() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
				String filePath = path.toAbsolutePath().toString();
				String newPath = getNewPath(filePath);
				// 创建目录或文件
				createDirOrFile(newPath);
				byte[] buff;
				if (filePath.endsWith(MODIFY_JAVA_FILE_SUFFIX)) {
					buff = getJavaFileAsByte(filePath);
				} else if (filePath.endsWith(MODIFY_POM_FILE_SUFFIX)) {
					buff = getPomFileAsByte(filePath);
				} else if (filePath.endsWith(MODIFY_XML_FILE_SUFFIX)) {
					buff = getXmlFileAsByte(filePath);
				} else {
					buff = Files.readAllBytes(Paths.get(filePath));
				}
				Files.write(Paths.get(newPath), buff);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
				boolean isExecute = false;
				String dir = path.toString();
				for (String module : MODULES) {
					if (dir.contains(module)) {
						isExecute = true;
						break;
					}
				}
				if (isExecute || count++ < 1) {
					return FileVisitResult.CONTINUE;
				}
				return FileVisitResult.SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				return FileVisitResult.CONTINUE;
			}

		});
	}

	private static void createDirOrFile(String path) {
		int index = path.lastIndexOf(File.separator);
		String dir = path.substring(0, index);
		String fileName = path.substring(index + 1);
		FileUtil.createFile(dir, fileName);
	}

	private static String getNewPath(String path) {
		return path.replaceAll("laokou-", NEW_MODULE_NAME + "-")
			.replaceAll(getOldPackagePath(), getNewPackagePath())
			.replace("KCloud-Platform-IoT", NEW_PROJECT_NAME);
	}

	private static byte[] getJavaFileAsByte(String path) throws IOException {
		String str = Files.readString(Paths.get(path));
		return str.replaceAll("org.laokou", NEW_PACKAGE_NAME).getBytes(StandardCharsets.UTF_8);
	}

	private static byte[] getPomFileAsByte(String path) throws IOException {
		String str = Files.readString(Paths.get(path));
		return str.replaceAll("org.laokou", NEW_GROUP_ID)
			.replaceAll("laokou-", NEW_MODULE_NAME + "-")
			.replace("KCloud-Platform-IoT", NEW_PROJECT_NAME)
			.getBytes(StandardCharsets.UTF_8);
	}

	private static byte[] getXmlFileAsByte(String path) throws IOException {
		String str = Files.readString(Paths.get(path));
		return str.replaceAll("org.laokou", NEW_PACKAGE_NAME).getBytes(StandardCharsets.UTF_8);
	}

	private static String getOldPackagePath() {
		return isWindows() ? "\\\\org\\\\laokou\\\\" : "/org/laokou/";
	}

	private static String getNewPackagePath() {
		return isWindows() ? NEW_PACKAGE_PATH_WINDOW : NEW_PACKAGE_PATH_LINUX;
	}

}
