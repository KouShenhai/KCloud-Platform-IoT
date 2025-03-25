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

package org.laokou.common.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * gson工具类.
 *
 * @author laokou
 */
public final class GsonUtils {

	private GsonUtils() {
	}

	/**
	 * 对象转为gson字符串.
	 * @param obj 对象
	 * @return gson字符串
	 */
	public static String toPrettyFormat(Object obj) {
		// 关闭html转义
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		return gson.toJson(obj);
	}

}
