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

package org.laokou.common.i18n.utils;

import java.util.Objects;

/**
 * 对象工具类.
 *
 * @author laokou
 */
public class ObjectUtil {

	public static boolean isNotNull(Object o) {
		return Objects.nonNull(o);
	}

	public static boolean isNull(Object o) {
		return Objects.isNull(o);
	}

	public static boolean equals(Object o1, Object o2) {
		return Objects.equals(o1, o2);
	}

	/**
	 * 哈希码.
	 * @param args 参数
	 * @return 哈希码
	 */
	public static int hash(Object... args) {
		return Objects.hash(args);
	}

	/**
	 * 对象不允许为空.
	 * @param obj 对象
	 * @param <T> 泛型
	 * @return 对象
	 */
	public static <T> T requireNotNull(T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

}
