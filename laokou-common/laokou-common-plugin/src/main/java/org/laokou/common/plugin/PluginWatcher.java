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
import org.laokou.common.core.util.ThreadUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 插件目录热加载监听器.
 *
 * <p>
 * 基于 Java NIO {@link WatchService} 监听插件目录的文件变化：
 * <ul>
 * <li>新 {@code .jar} 文件放入 → 自动加载并启动插件</li>
 * <li>{@code .jar} 文件被删除 → 自动停止并卸载插件</li>
 * <li>{@code .jar} 文件被替换（先删除后新增）→ 自动重新加载新版本</li>
 * </ul>
 * </p>
 *
 * <p>
 * 监听器在独立的守护线程中运行，通过 {@link #start()} 启动， 通过 {@link #stop()} 优雅停止。
 * </p>
 *
 * @author laokou
 */
@Slf4j
@Getter
public class PluginWatcher {

	private final File pluginDir;

	private final PluginManager pluginManager;

	private final long pollInterval;

	private final AtomicBoolean running = new AtomicBoolean(true);

	private final ExecutorService virtualThreadExecutor;

	private volatile WatchService watchService;

	/**
	 * JAR 文件名 → 插件 ID 的映射，用于删除时找到对应插件 ID.
	 */
	private final Map<String, String> jarToPluginId = new HashMap<>();

	/**
	 * 创建插件目录监听器.
	 * @param pluginDir 监听的插件目录
	 * @param pluginManager 插件管理器
	 * @param pollInterval 轮询间隔（毫秒）
	 */
	public PluginWatcher(File pluginDir, PluginManager pluginManager, long pollInterval) {
		this.pluginDir = pluginDir;
		this.pluginManager = pluginManager;
		this.pollInterval = pollInterval;
		this.virtualThreadExecutor = ThreadUtils.newVirtualTaskExecutor();
	}

	/**
	 * 启动目录监听（在守护线程中运行）.
	 */
	public void start() {
		if (running.compareAndSet(false, true)) {
			if (!pluginDir.exists() || !pluginDir.isDirectory()) {
				log.warn("[插件热加载] 插件目录不存在或不是目录，无法启动监听: {}", pluginDir.getAbsolutePath());
				return;
			}
			try {
				watchService = FileSystems.getDefault().newWatchService();
				pluginDir.toPath()
					.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
							StandardWatchEventKinds.ENTRY_MODIFY);
				running.set(true);
				virtualThreadExecutor.execute(this::watchLoop);
				log.info("[插件热加载] 目录监听已启动: {}", pluginDir.getAbsolutePath());
			}
			catch (IOException ex) {
				log.error("[插件热加载] 启动监听失败", ex);
			}
		}
		else {
			log.warn("[插件热加载] 监听器已在运行中，跳过重复启动");
		}
	}

	/**
	 * 停止目录监听.
	 */
	public void stop() {
		if (running.compareAndSet(true, false)) {
			if (watchService != null) {
				try {
					watchService.close();
				}
				catch (IOException ex) {
					log.warn("[插件热加载] 关闭 WatchService 失败", ex);
				}
			}
			ThreadUtils.shutdown(virtualThreadExecutor, 5000);
			log.info("【插件热加载】 目录监听已停止");
		}
	}

	/**
	 * 监听循环（在守护线程中持续运行）.
	 */
	private void watchLoop() {
		log.debug("[插件热加载] 监听线程启动");
		while (running.get()) {
			try {
				WatchKey key = watchService.poll(pollInterval, TimeUnit.MILLISECONDS);
				if (key == null) {
					continue;
				}
				for (WatchEvent<?> event : key.pollEvents()) {
					handleEvent(event);
				}
				boolean valid = key.reset();
				if (!valid) {
					log.warn("[插件热加载] 监听 Key 已失效，停止监听");
					break;
				}
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				log.debug("[插件热加载] 监听线程被中断");
				break;
			}
			catch (ClosedWatchServiceException ex) {
				log.debug("[插件热加载] WatchService 已关闭，退出监听循环");
				break;
			}
			catch (Exception ex) {
				log.error("[插件热加载] 监听异常", ex);
			}
		}
		log.debug("[插件热加载] 监听线程退出");
	}

	/**
	 * 处理单个文件事件.
	 */
	@SuppressWarnings("unchecked")
	private void handleEvent(WatchEvent<?> event) {
		WatchEvent.Kind<?> kind = event.kind();
		if (kind == StandardWatchEventKinds.OVERFLOW) {
			log.warn("[插件热加载] 事件溢出，部分事件可能丢失");
			return;
		}
		WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
		Path fileName = pathEvent.context();
		String name = fileName.toString();

		// 只处理 .jar 文件
		if (!name.endsWith(".jar")) {
			return;
		}

		File jarFile = new File(pluginDir, name);

		if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
			onJarCreated(jarFile);
		}
		else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
			onJarDeleted(name);
		}
		else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
			onJarModified(jarFile, name);
		}
	}

	/**
	 * 新 JAR 文件放入：等待文件写入完成后加载.
	 */
	private void onJarCreated(File jarFile) {
		log.info("[插件热加载] 检测到新插件 JAR: {}", jarFile.getName());
		// 等待文件写入稳定（避免复制过程中触发加载）
		waitForFileStable(jarFile);
		try {
			PluginContext context = pluginManager.load(jarFile);
			// 记录 JAR 文件名 → 插件 ID 映射
			jarToPluginId.put(jarFile.getName(), context.getId());
			log.info("[插件热加载] 插件自动加载成功: jar={}, id={}", jarFile.getName(), context.getId());
		}
		catch (Exception ex) {
			log.error("[插件热加载] 插件自动加载失败: {}", jarFile.getName(), ex);
		}
	}

	/**
	 * JAR 文件被删除：卸载对应插件.
	 */
	private void onJarDeleted(String jarName) {
		String pluginId = jarToPluginId.remove(jarName);
		if (pluginId == null) {
			// 可能是启动前就存在的 JAR，尝试从注册表中查找
			pluginId = findPluginIdByJarName(jarName);
		}
		if (pluginId != null) {
			log.info("[插件热加载] 检测到插件 JAR 被删除，开始卸载: jar={}, id={}", jarName, pluginId);
			try {
				pluginManager.unload(pluginId);
				log.info("[插件热加载] 插件自动卸载成功: id={}", pluginId);
			}
			catch (Exception ex) {
				log.error("[插件热加载] 插件自动卸载失败: id={}", pluginId, ex);
			}
		}
		else {
			log.debug("[插件热加载] 删除的 JAR 未在注册表中找到对应插件: {}", jarName);
		}
	}

	/**
	 * JAR 文件被修改（替换升级）：先卸载旧版本，再加载新版本.
	 */
	private void onJarModified(File jarFile, String jarName) {
		log.info("[插件热加载] 检测到插件 JAR 被修改（版本升级）: {}", jarName);
		// 先卸载旧版本
		onJarDeleted(jarName);
		// 再加载新版本
		waitForFileStable(jarFile);
		onJarCreated(jarFile);
	}

	/**
	 * 等待 JAR 文件写入稳定（大小不再变化）. 避免文件还在复制时就触发加载。
	 */
	private void waitForFileStable(File file) {
		long lastSize = -1;
		int stableCount = 0;
		while (stableCount < 3) {
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				return;
			}
			long currentSize = file.length();
			if (currentSize == lastSize) {
				stableCount++;
			}
			else {
				stableCount = 0;
				lastSize = currentSize;
			}
		}
	}

	/**
	 * 根据 JAR 文件名从已注册的插件中推断插件 ID（用于处理启动前已存在的 JAR）. 约定：JAR 文件名去掉 .jar 后缀即为插件 ID（如
	 * my-plugin-1.0.0.jar → my-plugin-1.0.0）.
	 */
	private String findPluginIdByJarName(String jarName) {
		// 先从所有已注册插件的元数据中匹配
		return pluginManager.getAll().stream().filter(ctx -> {
			String id = ctx.getId();
			// 支持 "pluginId.jar" 或 "pluginId-version.jar" 命名约定
			return jarName.startsWith(id);
		}).map(PluginContext::getId).findFirst().orElse(null);
	}

}
