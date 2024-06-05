/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.core.utils;

import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;

/**
 * @author laokou
 */
public class SerializationUtil {

	private static final ThreadSafeFury THREAD_SAFE_FURY = Fury.builder()
		.withLanguage(Language.JAVA)
		.requireClassRegistration(true)
		.buildThreadSafeFury();

	private static final Fury FURY = Fury.builder().withLanguage(Language.JAVA).requireClassRegistration(true).build();

	public static byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}
		return FURY.serialize(object);
	}

	public static Object deserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return FURY.deserialize(bytes);
	}

	public static byte[] serializeThreadSafe(Object object) {
		if (object == null) {
			return null;
		}
		return THREAD_SAFE_FURY.serialize(object);
	}

	public static Object deserializeThreadSafe(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return THREAD_SAFE_FURY.deserialize(bytes);
	}

}
