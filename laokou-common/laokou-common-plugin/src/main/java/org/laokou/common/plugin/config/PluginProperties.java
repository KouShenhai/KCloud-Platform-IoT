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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 插件系统配置属性.
 *
 * <p>
 * 在 application.yml 中配置示例： <pre>
 * network:
 *   plugin:
 *     enabled: true
 *     dir: plugins
 *     autoload: true
 *     watch: true
 *     watch-interval: 2000
 * </pre>
 * </p>
 *
 * @author laokou
 */
@Data
@ConfigurationProperties(prefix = "network.plugin")
public class PluginProperties {

	/**
	 * 是否启用插件系统，默认 true.
	 */
	private boolean enabled = true;

	/**
	 * 插件目录路径（相对于工作目录或绝对路径），默认 plugins.
	 */
	private String dir = "plugins";

	/**
	 * 是否在容器启动时自动加载插件目录下的所有 JAR，默认 true.
	 */
	private boolean autoload = true;

	/**
	 * 是否开启目录监听（热加载），运行时放入新 JAR 自动加载，删除 JAR 自动卸载，默认 false.
	 */
	private boolean watch = false;

	/**
	 * 目录监听轮询间隔（毫秒），默认 2000ms. 仅在 watch=true 时生效.
	 */
	private long watchInterval = 2000L;

}
