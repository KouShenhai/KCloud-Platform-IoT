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

/**
 * 插件基类，所有插件必须继承此类.
 * <p>
 * 生命周期：INIT → LOADED（onLoad）→ STARTED（onStart）→ STOPPED（onStop）→ DESTROYED（onDestroy）
 * </p>
 *
 * @author laokou
 */
public abstract class Plugin {

	/**
	 * 插件加载完成后调用（类加载、实例化完成）.
	 */
	public void onLoad() {
	}

	/**
	 * 插件启动时调用（容器启动后）.
	 */
	public void onStart() {
	}

	/**
	 * 插件停止时调用（容器关闭前）.
	 */
	public void onStop() {
	}

	/**
	 * 插件销毁时调用（资源释放）.
	 */
	public void onDestroy() {
	}

}
