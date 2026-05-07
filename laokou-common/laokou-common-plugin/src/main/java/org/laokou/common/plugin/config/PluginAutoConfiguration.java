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

package org.laokou.common.plugin.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.plugin.PluginManager;
import org.laokou.common.plugin.PluginWatcher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;

import java.io.File;

/**
 * 插件系统 Spring Boot 自动配置.
 *
 * <p>
 * 当 {@code laokou.plugin.enabled=true}（默认值）时生效，自动注册 {@link PluginManager} Bean，并在 Spring
 * 容器完全启动后（通过 {@link SmartLifecycle}） 自动扫描并加载插件目录。若 {@code laokou.plugin.watch=true}，还会启动
 * {@link PluginWatcher} 实现运行时热加载。
 * </p>
 *
 * @author laokou
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(PluginProperties.class)
@ConditionalOnProperty(prefix = "network.plugin", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PluginAutoConfiguration {

	/**
	 * 注册 {@link PluginManager} Bean.
	 * @param properties 插件配置属性
	 * @return 插件管理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public PluginManager pluginManager(PluginProperties properties) {
		File pluginDir = resolvePluginDir(properties.getDir());
		return new PluginManager(pluginDir);
	}

	/**
	 * 注册插件生命周期管理器，实现 SmartLifecycle 以便在容器完全启动/关闭时自动加载/卸载插件. 若
	 * {@code laokou.plugin.watch=true}，同时启停 {@link PluginWatcher} 实现热加载。
	 * @param pluginManager 插件管理器
	 * @param properties 插件配置属性
	 * @return SmartLifecycle 实例
	 */
	@Bean
	public SmartLifecycle pluginLifecycle(PluginManager pluginManager, PluginProperties properties) {
		return new SmartLifecycle() {

			private volatile boolean running = false;

			private PluginWatcher watcher;

			@Override
			public void start() {
				// 1. 自动加载已有插件
				if (properties.isAutoload()) {
					log.info("[插件系统] 容器启动完成，自动加载插件...");
					pluginManager.loadAll();
				}

				// 2. 启动热加载监听（可选）
				if (properties.isWatch()) {
					watcher = new PluginWatcher(pluginManager.getPluginDir(), pluginManager,
							properties.getWatchInterval());
					watcher.start();
				}

				running = true;
			}

			@Override
			public void stop() {
				// 1. 停止热加载监听
				if (watcher != null && watcher.getRunning().get()) {
					watcher.stop();
				}

				// 2. 卸载所有插件
				log.info("[插件系统] 容器关闭，卸载所有插件...");
				pluginManager.unloadAll();

				running = false;
			}

			@Override
			public boolean isRunning() {
				return running;
			}

			/**
			 * 最低优先级（最后启动），确保业务 Bean 均已初始化完毕再加载插件.
			 */
			@Override
			public int getPhase() {
				return Integer.MAX_VALUE;
			}

		};
	}

	/**
	 * 解析插件目录路径（相对路径转换为绝对路径）.
	 */
	private File resolvePluginDir(String dir) {
		File pluginDir = new File(dir);
		if (!pluginDir.isAbsolute()) {
			pluginDir = new File(System.getProperty("user.dir"), dir);
		}
		log.info("[插件系统] 插件目录: {}", pluginDir.getAbsolutePath());
		if (!pluginDir.exists()) {
			boolean created = pluginDir.mkdirs();
			if (created) {
				log.info("[插件系统] 插件目录已创建: {}", pluginDir.getAbsolutePath());
			}
		}
		return pluginDir;
	}

}
