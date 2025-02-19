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

import io.netty.util.concurrent.Future;

/**
 * @author laokou
 */
public interface Server {

	/**
	 * 发送消息.
	 * @param obj 对象
	 * @param key 标识
	 */
	Future<Void> send(String key, Object obj);

	/**
	 * 启动.
	 */
	void start();

	/**
	 * 停止.
	 */
	void stop();

}
