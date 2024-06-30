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

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;

import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constants.StringConstant.SLASH;

/**
 * 一键修改项目.
 * @author laokou
 */
public class ModifyProjectBoot {

	private static final String FILE_NAME = "pom.xml";

	public static void main(String[] args) throws IOException, XmlPullParserException {
		// 修改projectName、packageName、groupId、artifactId
		MavenXpp3Reader reader = new MavenXpp3Reader();
		recursionModule(System.getProperty("user.dir") + SLASH + FILE_NAME, reader);
	}

	private static void recursionModule(String path, MavenXpp3Reader reader) throws IOException, XmlPullParserException {
		Model model = reader.read(new FileReader(path));
		model.getModules().forEach(moduleName -> {
			try {
				recursionModule(path.replace(FILE_NAME, EMPTY) + moduleName + SLASH + FILE_NAME, reader);
			} catch (IOException | XmlPullParserException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
