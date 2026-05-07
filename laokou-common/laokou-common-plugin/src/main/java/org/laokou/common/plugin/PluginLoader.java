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

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.plugin.exception.PluginLoadException;
import org.laokou.common.plugin.model.PluginState;
import org.laokou.common.plugin.util.PluginClassLoader;
import org.laokou.common.plugin.util.PluginUtils;

import java.io.File;
import java.io.IOException;

/**
 * 插件 JAR 加载器，负责.
 * <ol>
 * <li>从 JAR 中读取 {@code plugin.yml} 元数据</li>
 * <li>使用 {@link PluginClassLoader} 加载 mainClass</li>
 * <li>实例化插件并回调 {@link Plugin#onLoad()}</li>
 * <li>将插件上下文注册到 {@link PluginRegistry}</li>
 * </ol>
 *
 * @author laokou
 */
@Slf4j
public record PluginLoader(PluginRegistry registry) {

	/**
	 * 加载单个插件 JAR 文件.
	 * @param jarFile 插件 JAR 文件
	 * @return 已加载的插件上下文
	 * @throws PluginLoadException 加载失败时抛出
	 */
	public PluginContext load(File jarFile) {
		log.info("【插件加载】 开始加载插件: {}", jarFile.getAbsolutePath());

		// 1. 读取 plugin.yml 元数据
		PluginMetadata metadata = PluginUtils.loadPluginMetadata(jarFile);
		String pluginId = metadata.getId();

		// 2. 检查是否重复注册
		if (registry.contains(pluginId)) {
			throw new PluginLoadException("B_Plugin_AlreadyLoaded", "Plugin already loaded: " + pluginId);
		}

		// 3. 创建专属类加载器并加载 JAR
		PluginClassLoader classLoader = new PluginClassLoader(Thread.currentThread().getContextClassLoader());
		try {
			classLoader.addJar(jarFile.toURI().toURL());
		}
		catch (Exception ex) {
			closeQuietly(classLoader);
			throw new PluginLoadException("B_Plugin_ClassLoaderFailed",
					"Failed to add JAR to classloader: " + jarFile.getName(), ex);
		}

		// 4. 加载 mainClass 并实例化
		Plugin plugin = instantiatePlugin(metadata.getMainClass(), classLoader);

		// 5. 构建上下文并触发 onLoad 钩子
		PluginContext context = new PluginContext(metadata, plugin, classLoader);
		try {
			plugin.onLoad();
			context.compareAndSetState(PluginState.INIT, PluginState.LOADED);
			log.info("【插件加载】 插件 onLoad() 完成: id={}", pluginId);
		}
		catch (Exception ex) {
			context.setState(PluginState.ERROR);
			closeQuietly(classLoader);
			throw new PluginLoadException("B_Plugin_OnLoadFailed", "Plugin onLoad() failed: " + pluginId, ex);
		}

		// 6. 注册到注册表
		registry.register(context);
		return context;
	}

	/**
	 * 触发已加载插件的 onStart 生命周期.
	 * @param context 插件上下文
	 */
	public void start(PluginContext context) {
		if (!context.compareAndSetState(PluginState.LOADED, PluginState.STARTED)) {
			log.warn("【插件启动】 跳过（状态不是 LOADED）: id={}, state={}", context.getId(), context.getState());
			return;
		}
		try {
			context.getPlugin().onStart();
			log.info("【插件启动】 onStart() 完成: id={}", context.getId());
		}
		catch (Exception ex) {
			context.setState(PluginState.ERROR);
			throw new PluginLoadException("B_Plugin_OnStartFailed", "Plugin onStart() failed: " + context.getId(), ex);
		}
	}

	/**
	 * 卸载插件：触发 onStop、onDestroy，关闭类加载器，从注册表移除.
	 * @param pluginId 插件 ID
	 */
	public void unload(String pluginId) {
		registry.unregister(pluginId).ifPresent(context -> {
			log.info("【插件卸载】 开始卸载插件: id={}", pluginId);
			Plugin plugin = context.getPlugin();

			// onStop
			if (context.getState() == PluginState.STARTED) {
				try {
					plugin.onStop();
					context.compareAndSetState(PluginState.STARTED, PluginState.STOPPED);
					log.info("【插件卸载】 onStop() 完成: id={}", pluginId);
				}
				catch (Exception ex) {
					log.error("【插件卸载】 onStop() 异常: id={}", pluginId, ex);
					context.setState(PluginState.ERROR);
				}
			}

			// onDestroy
			try {
				plugin.onDestroy();
				context.setState(PluginState.DESTROYED);
				log.info("【插件卸载】 onDestroy() 完成: id={}", pluginId);
			}
			catch (Exception ex) {
				log.error("【插件卸载】 onDestroy() 异常: id={}", pluginId, ex);
			}

			// 关闭类加载器
			closeQuietly(context.getClassLoader());
		});
	}

	/**
	 * 通过反射实例化插件主类.
	 */
	private Plugin instantiatePlugin(String mainClass, ClassLoader classLoader) {
		try {
			Class<?> clazz = classLoader.loadClass(mainClass);
			if (!Plugin.class.isAssignableFrom(clazz)) {
				throw new PluginLoadException("B_Plugin_InvalidMainClass",
						"Main class does not extend Plugin: " + mainClass);
			}
			return (Plugin) clazz.getDeclaredConstructor().newInstance();
		}
		catch (PluginLoadException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new PluginLoadException("B_Plugin_InstantiateFailed",
					"Failed to instantiate plugin main class: " + mainClass, ex);
		}
	}

	/**
	 * 静默关闭类加载器.
	 */
	private void closeQuietly(PluginClassLoader classLoader) {
		try {
			PluginUtils.closeQuietly(classLoader);
		}
		catch (IOException ex) {
			log.warn("【插件加载】 关闭类加载器失败", ex);
		}
	}

}
