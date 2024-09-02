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

import org.springframework.util.ObjectUtils;

import java.util.Arrays;

/**
 * 数据工具类.
 *
 * @author laokou
 */
public final class ArrayUtil {

	public static boolean isNotEmpty(byte[] array) {
		return !isEmpty(array);
	}

	public static boolean isEmpty(byte[] array) {
		return array == null || Arrays.equals(array, new byte[0]);
	}

	/**
	 * 判断数组不为空.
	 * @param array 数组
	 * @return 判断结果
	 */
	public static boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}

	/**
	 * 判断数组为空.
	 * @param array 数组
	 * @return 判断结果
	 */
	public static boolean isEmpty(Object[] array) {
		return ObjectUtils.isEmpty(array);
	}

}
