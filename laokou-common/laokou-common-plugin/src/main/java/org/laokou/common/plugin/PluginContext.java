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
import org.laokou.common.plugin.model.PluginState;
import org.laokou.common.plugin.util.PluginClassLoader;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 插件运行时上下文，持有插件元数据、实例、类加载器及当前状态.
 *
 * @author laokou
 */
@Getter
public class PluginContext {

	/**
	 * 插件元数据.
	 */
	private final PluginMetadata metadata;

	/**
	 * 插件实例.
	 */
	private final Plugin plugin;

	/**
	 * 插件专属类加载器.
	 */
	private final PluginClassLoader classLoader;

	/**
	 * 插件当前状态（原子引用，线程安全）.
	 */
	private final AtomicReference<PluginState> state;

	public PluginContext(PluginMetadata metadata, Plugin plugin, PluginClassLoader classLoader) {
		this.metadata = metadata;
		this.plugin = plugin;
		this.classLoader = classLoader;
		this.state = new AtomicReference<>(PluginState.INIT);
	}

	/**
	 * 获取当前插件状态.
	 * @return 当前状态
	 */
	public PluginState getState() {
		return state.get();
	}

	/**
	 * 尝试原子性地将状态从 expected 切换到 next.
	 * @param expected 期望当前状态
	 * @param next 目标状态
	 * @return 是否切换成功
	 */
	public boolean compareAndSetState(PluginState expected, PluginState next) {
		return state.compareAndSet(expected, next);
	}

	/**
	 * 强制设置状态（用于错误处理）.
	 * @param newState 新状态
	 */
	public void setState(PluginState newState) {
		state.set(newState);
	}

	/**
	 * 获取插件 ID（快捷方法）.
	 * @return 插件 ID
	 */
	public String getId() {
		return metadata.getId();
	}

}
