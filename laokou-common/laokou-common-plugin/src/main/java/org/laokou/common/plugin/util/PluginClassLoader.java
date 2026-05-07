/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.plugin.util;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 插件类加载器，隔离插件依赖，支持动态添加 JAR.
 *
 * @author laokou
 */
public class PluginClassLoader extends URLClassLoader {

	public PluginClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}

	/**
	 * 向此 ClassLoader 添加 JAR 文件.
	 * @param url JAR 文件的 URL
	 */
	public void addJar(URL url) {
		addURL(url);
	}

}
