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

package org.laokou.common.mqtt.client;

import org.apache.fory.ThreadSafeFory;
import org.apache.fory.config.ForyBuilder;

import java.util.Arrays;

/**
 * @author laokou
 */
public final class MqttForyFactory {

	public static final MqttForyFactory INSTANCE = new MqttForyFactory();

	private final ThreadSafeFory fory;

	public MqttForyFactory() {
		fory = new ForyBuilder()
			// 开启多语言序列化
			.withXlang(true)
			// 关闭循环引用跟踪
			.withRefTracking(false)
			.buildThreadSafeFory();
	}

	public Object deserialize(byte[] bytes) {
		if (bytes == null || Arrays.equals(bytes, new byte[0])) {
			return null;
		}
		return fory.deserialize(bytes);
	}

}
