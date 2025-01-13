/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.netty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WebSocketServer属性配置.
 *
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.websocket-server")
public class SpringWebSocketServerProperties {

	/**
	 * IP.
	 */
	private String ip;

	/**
	 * 端口.
	 */
	private int port;

	/**
	 * 服务ID.
	 */
	private String serviceId;

	/**
	 * 最大内容长度.
	 */
	private int maxContentLength = 65536;

	/**
	 * WebSocket路径.
	 */
	private String websocketPath = "/ws";

	/**
	 * 显示刷新Flush大小.
	 */
	private int explicitFlushAfterFlushes = 10;

	/**
	 * 当没有正在进行的读取时进行合并.
	 */
	private boolean consolidateWhenNoReadInProgress = true;

	/**
	 * 读取空闲时间.
	 */
	private long readerIdleTime = 60;

	/**
	 * 写入空闲时间.
	 */
	private long writerIdleTime = 0;

	/**
	 * 全部时间.
	 */
	private long allIdleTime = 0;

	/**
	 * 监听核心线程数.
	 */
	private Integer bossCorePoolSize = 1;

	/**
	 * 读写核心线程数.
	 */
	private Integer workerCorePoolSize = 8;

	/**
	 * 组核心线程数.
	 */
	private Integer groupCorePoolSize = 16;

	/**
	 * 延迟发送 => true实时，false延迟.
	 */
	private boolean tcpNodelay = false;

	/**
	 * 请求队列最大长度.
	 */
	private int backlogLength = 1024;

	/**
	 * 开启心跳包活机制.
	 */
	private boolean keepAlive = true;

	/**
	 * 最大心跳次数.
	 */
	private int maxHeartBeatCount = 5;

}
