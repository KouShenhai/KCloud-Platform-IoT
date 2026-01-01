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

package org.laokou.common.plugin.model;

import lombok.Getter;

/**
 * 插件状态.
 *
 * @author laokou
 */
@Getter
public enum PluginStateEnum {

	INIT("init", "初始化状态"),

	LOADED("loaded", "已加载状态（插件已加载但未启动）"),

	STARTED("started", "已启动状态（插件正在运行）"),

	STOPPED("stopped", "已停止状态（插件已停止但未销毁）"),

	DESTROYED("destroyed", "已销毁状态（插件已完全卸载）"),

	ERROR("error", "错误状态（插件出现异常）");

	private final String code;

	private final String desc;

	PluginStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
