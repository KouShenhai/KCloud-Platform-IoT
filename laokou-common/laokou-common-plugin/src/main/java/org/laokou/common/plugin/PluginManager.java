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

package org.laokou.common.plugin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 插件管理器，对外提供插件的扫描加载、查询、热卸载等统一接口.
 *
 * <p>
 * 典型使用流程： <pre>
 *   PluginManager manager = new PluginManager(pluginDir);
 *   manager.loadAll();        // 批量加载 pluginDir 下所有 .jar
 *   manager.get("my-plugin"); // 获取指定插件上下文
 *   manager.unload("my-plugin"); // 热卸载插件
 * </pre>
 * </p>
 *
 * @author laokou
 */
@Slf4j
@Getter
public class PluginManager {

	private final File pluginDir;

	private final PluginRegistry registry;

	private final PluginLoader loader;

	public PluginManager(File pluginDir) {
		this.pluginDir = pluginDir;
		this.registry = new PluginRegistry();
		this.loader = new PluginLoader(registry);
	}

	/**
	 * 扫描插件目录，加载并启动所有 .jar 插件.
	 */
	public void loadAll() {
		List<PluginContext> loaded = new ArrayList<>();

		if (!pluginDir.exists()) {
			log.info("【插件管理】 插件目录不存在，跳过加载: {}", pluginDir.getAbsolutePath());
			return;
		}
		if (!pluginDir.isDirectory()) {
			log.warn("【插件管理】 插件路径不是目录: {}", pluginDir.getAbsolutePath());
			return;
		}

		File[] jarFiles = pluginDir.listFiles(f -> f.isFile() && f.getName().endsWith(".jar"));
		if (jarFiles == null || jarFiles.length == 0) {
			log.info("【插件管理】 插件目录下无 .jar 文件: {}", pluginDir.getAbsolutePath());
			return;
		}

		log.info("【插件管理】 发现 {} 个插件 JAR，开始加载...", jarFiles.length);
		for (File jarFile : jarFiles) {
			try {
				PluginContext context = loadAndStart(jarFile);
				loaded.add(context);
			}
			catch (Exception ex) {
				log.error("【插件管理】 加载插件失败，已跳过: {}", jarFile.getName(), ex);
			}
		}
		log.info("【插件管理】 批量加载完成，成功: {}/{}", loaded.size(), jarFiles.length);
	}

	/**
	 * 加载并启动单个插件 JAR 文件.
	 * @param jarFile JAR 文件
	 * @return 插件上下文
	 */
	public PluginContext load(File jarFile) {
		return loadAndStart(jarFile);
	}

	/**
	 * 热卸载指定插件.
	 * @param pluginId 插件 ID
	 */
	public void unload(String pluginId) {
		loader.unload(pluginId);
	}

	/**
	 * 停止并卸载所有已加载的插件（通常在容器关闭时调用）.
	 */
	public void unloadAll() {
		log.info("【插件管理】 开始卸载所有插件，数量: {}", registry.size());
		// 复制 ID 列表避免 ConcurrentModificationException
		List<String> ids = registry.getAll().stream().map(PluginContext::getId).toList();
		for (String id : ids) {
			try {
				loader.unload(id);
			}
			catch (Exception ex) {
				log.error("【插件管理】 卸载插件异常: id={}", id, ex);
			}
		}
		log.info("【插件管理】 所有插件已卸载");
	}

	/**
	 * 根据插件 ID 获取插件上下文.
	 * @param pluginId 插件 ID
	 * @return 插件上下文（不存在时抛出异常）
	 */
	public PluginContext get(String pluginId) {
		return registry.get(pluginId);
	}

	/**
	 * 根据插件 ID 查找插件上下文（不抛异常）.
	 * @param pluginId 插件 ID
	 * @return Optional 包装
	 */
	public Optional<PluginContext> find(String pluginId) {
		return registry.find(pluginId);
	}

	/**
	 * 获取所有已加载的插件上下文.
	 * @return 只读集合
	 */
	public Collection<PluginContext> getAll() {
		return registry.getAll();
	}

	private PluginContext loadAndStart(File jarFile) {
		PluginContext context = loader.load(jarFile);
		loader.start(context);
		return context;
	}

}
