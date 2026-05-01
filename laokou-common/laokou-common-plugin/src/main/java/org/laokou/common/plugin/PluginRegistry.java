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
import org.laokou.common.plugin.exception.PluginNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 插件注册表，以插件 ID 为 Key 存储所有已注册的插件上下文. 线程安全。
 *
 * @author laokou
 */
@Slf4j
public class PluginRegistry {

	private final ConcurrentMap<String, PluginContext> registry = new ConcurrentHashMap<>();

	/**
	 * 注册插件上下文.
	 * @param context 插件上下文
	 * @throws IllegalArgumentException 若同 ID 插件已存在
	 */
	public void register(PluginContext context) {
		String id = context.getId();
		if (registry.putIfAbsent(id, context) != null) {
			throw new IllegalArgumentException("Plugin already registered: " + id);
		}
		log.info("【插件注册】 id={}, name={}, version={}", id, context.getMetadata().getName(),
				context.getMetadata().getVersion());
	}

	/**
	 * 取消注册插件.
	 * @param pluginId 插件 ID
	 * @return 被移除的上下文，若不存在则返回 empty
	 */
	public Optional<PluginContext> unregister(String pluginId) {
		PluginContext removed = registry.remove(pluginId);
		if (removed != null) {
			log.info("【插件注销】 id={}", pluginId);
		}
		return Optional.ofNullable(removed);
	}

	/**
	 * 根据 ID 获取插件上下文.
	 * @param pluginId 插件 ID
	 * @return 插件上下文
	 * @throws PluginNotFoundException 若插件不存在
	 */
	public PluginContext get(String pluginId) {
		PluginContext context = registry.get(pluginId);
		if (context == null) {
			throw new PluginNotFoundException("B_Plugin_NotFound", "Plugin not found: " + pluginId);
		}
		return context;
	}

	/**
	 * 查找插件上下文（不抛异常）.
	 * @param pluginId 插件 ID
	 * @return Optional 包装的上下文
	 */
	public Optional<PluginContext> find(String pluginId) {
		return Optional.ofNullable(registry.get(pluginId));
	}

	/**
	 * 检查插件是否已注册.
	 * @param pluginId 插件 ID
	 * @return 是否存在
	 */
	public boolean contains(String pluginId) {
		return registry.containsKey(pluginId);
	}

	/**
	 * 获取所有已注册的插件上下文（只读视图）.
	 * @return 插件上下文集合
	 */
	public Collection<PluginContext> getAll() {
		return Collections.unmodifiableCollection(registry.values());
	}

	/**
	 * 获取已注册插件数量.
	 * @return 插件数量
	 */
	public int size() {
		return registry.size();
	}

}
