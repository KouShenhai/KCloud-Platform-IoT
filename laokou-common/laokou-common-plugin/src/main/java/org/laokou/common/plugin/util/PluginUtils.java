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

import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.i18n.util.YamlUtils;
import org.laokou.common.plugin.PluginMetadata;
import org.laokou.common.plugin.exception.PluginLoadException;
import org.laokou.common.plugin.exception.PluginNotFoundException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author laokou
 */
public final class PluginUtils {

	private PluginUtils() {
	}

	public static PluginMetadata loadPluginMetadata(File jarFile) {
		try (JarFile jar = new JarFile(jarFile)) {
			JarEntry entry = jar.getJarEntry("plugin.yml");
			if (ObjectUtils.isNull(entry)) {
				throw new PluginNotFoundException("B_Plugin_YamlConfigNotFound",
						"plugin.yml not found in " + jarFile.getName());
			}
			try (InputStream inputStream = jar.getInputStream(entry)) {
				return validatePluginMetadata(inputStream);
			}
		}
		catch (IOException ex) {
			throw new PluginLoadException("B_Plugin_LoadFailed",
					"Failed to load plugin metadata from " + jarFile.getName(), ex);
		}
	}

	public static void closeQuietly(ClassLoader classLoader) throws IOException {
		if (classLoader instanceof URLClassLoader urlClassLoader) {
			urlClassLoader.close();
		}
	}

	private static PluginMetadata validatePluginMetadata(InputStream inputStream) {
		PluginMetadata pluginMetadata = YamlUtils.load(inputStream, PluginMetadata.class);
		if (StringExtUtils.isEmpty(pluginMetadata.getId())) {
			throw new PluginLoadException("B_Plugin_IdRequired", "Plugin Id is required");
		}
		if (StringExtUtils.isEmpty(pluginMetadata.getName())) {
			throw new PluginLoadException("B_Plugin_NameRequired", "Plugin Name is required");
		}
		if (StringExtUtils.isEmpty(pluginMetadata.getVersion())) {
			throw new PluginLoadException("B_Plugin_VersionRequired", "Plugin Version is required");
		}
		if (StringExtUtils.isEmpty(pluginMetadata.getMainClass())) {
			throw new PluginLoadException("B_Plugin_MainClassRequired", "Plugin MainClass is required");
		}
		return pluginMetadata;
	}

}
