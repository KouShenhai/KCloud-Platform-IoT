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

package org.laokou.core;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * 一键修改项目.
 * @author laokou
 */
public class ModifyProjectBoot {

	// -------------------------------------------------------------------------不可修改-------------------------------------------------------------------------
	private static int count = 0;
	private static final List<String> MODULES = List.of("laokou-cloud", "laokou-common", "laokou-service");
	private static final String MODIFY_POM_FILE_SUFFIX = "pom.xml";
	private static final String MODIFY_JAVA_FILE_SUFFIX = ".java";
	private static final MavenXpp3Reader XML_READER = new MavenXpp3Reader();
	// -------------------------------------------------------------------------不可修改-------------------------------------------------------------------------

	// -------------------------------------------------------------------------需要修改-------------------------------------------------------------------------
	// 新项目名称
	private static final String NEW_PROJECT_NAME = "new_laokou";
	// 新模块名称
	private static final String NEW_MODULE_NAME = "newlaokou";
	// 新分组ID
	private static final String NEW_GROUP_ID = "new.laokou";
	// -------------------------------------------------------------------------需要修改-------------------------------------------------------------------------

	public static void main(String[] args) throws IOException {
		// 修改projectName、packageName、groupId、artifactId
		String projectPath = System.getProperty("user.dir");
		Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<>() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				String filePath = path.toAbsolutePath().toString();
				// 只修改Java和Maven文件
				// 写入新文件夹
				String newPath = getNewPath(filePath);
				System.out.println(newPath);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
				boolean isSkip = false;
				String dir = path.toString();
				for (String module : MODULES) {
					if (dir.contains(module)) {
						isSkip = true;
						break;
					}
				}
				if (isSkip || count++ < 1) {
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

	private static String getNewPath(String path) {
		return path.replace("KCloud-Platform-IoT", NEW_PROJECT_NAME)
			.replace("laokou", NEW_MODULE_NAME);
	}

	/*private static void recursionModule(String path, MavenXpp3Reader reader) throws IOException, XmlPullParserException {
		Model model = reader.read(new FileReader(path));
		model.getModules().forEach(moduleName -> {
			try {
				recursionModule(path.replace(FILE_NAME, EMPTY) + moduleName + SLASH + FILE_NAME, reader);
			} catch (IOException | XmlPullParserException e) {
				throw new RuntimeException(e);
			}
		});
	}*/

}
