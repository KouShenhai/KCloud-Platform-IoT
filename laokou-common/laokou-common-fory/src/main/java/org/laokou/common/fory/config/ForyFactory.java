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

package org.laokou.common.fory.config;

import lombok.Getter;
import org.apache.fory.ThreadSafeFory;
import org.apache.fory.config.CompatibleMode;
import org.apache.fory.config.ForyBuilder;
import org.apache.fory.config.Language;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Getter
public final class ForyFactory {

	public static final ForyFactory INSTANCE = new ForyFactory();

	private final ThreadSafeFory fory = new ForyBuilder()
		// 关闭多语言序列化
		.withXlang(false)
		// 启用JAVA序列化
		.withLanguage(Language.JAVA)
		// 启用循环引用引用跟踪.
		.withRefTracking(true)
		// 压缩整数以节省空间
		.withIntCompressed(true)
		// 压缩长整数以节省空间
		.withLongCompressed(true)
		// CompatibleMode.SCHEMA_CONSISTENT模式序列化对象
		.withCompatibleMode(CompatibleMode.SCHEMA_CONSISTENT)
		// 启用异步多线程编译
		.withAsyncCompilation(true)
		// 启用类注册
		.requireClassRegistration(true)
		// 关闭反序列化不存在或未知的类
		.withDeserializeUnknownClass(false)
		// 限制嵌套反序列化深度
		.withMaxDepth(100)
		// 启用运行时代码生成
		.withCodegen(true)
		.buildThreadSafeFory();

	public <T> void register(Class<T> clazz, int num) {
		fory.register(clazz, num);
	}

	public <T> void register(Class<T> clazz) {
		fory.register(clazz);
	}

	public byte[] serialize(Object object) {
		if (object == null) {
			return new byte[0];
		}
		if (object instanceof String str) {
			return str.getBytes(StandardCharsets.UTF_8);
		}
		return fory.serialize(object);
	}

	public Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return fory.deserialize(bytes);
	}

}
