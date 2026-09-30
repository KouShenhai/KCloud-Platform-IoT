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

package org.laokou.common.fory.util;

import org.apache.fory.json.ForyJson;

/**
 * @author laokou
 */
public final class ForyJsonUtils {

	private static final ForyJson JSON = ForyJson.builder().build();

	private ForyJsonUtils() {
	}

	public static String toJsonStr(Object obj) {
		return JSON.toJson(obj);
	}

	public static <T> T toBean(String json, Class<T> clazz) {
		return JSON.fromJson(json, clazz);
	}

	public static <T> T toBean(byte[] bytes, Class<T> clazz) {
		return JSON.fromJson(bytes, clazz);
	}

}
