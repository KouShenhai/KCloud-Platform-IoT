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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合工具类.
 *
 * @author laokou
 */
public final class CollectionUtils {

	private CollectionUtils() {
	}

	/**
	 * 判断集合不为空.
	 * @param collection 集合
	 * @return 判断结果
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * 判断集合为空.
	 * @param collection 集合
	 * @return 判断结果
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return org.springframework.util.CollectionUtils.isEmpty(collection);
	}

	/**
	 * 集合转字符串.
	 * @param list 集合
	 * @param on 分隔符
	 * @return 字符串
	 */
	public static String toStr(List<String> list, String on) {
		if (list.isEmpty()) {
			return StringConstants.EMPTY;
		}
		return Joiner.on(on).skipNulls().join(list);
	}

	/**
	 * 字符串转集合.
	 * @param str 字符串
	 * @param on 分隔符
	 * @return 集合
	 */
	public static List<String> toList(String str, String on) {
		if (StringUtils.isEmpty(str)) {
			return new ArrayList<>(0);
		}
		return Splitter.on(on).trimResults().splitToList(str);
	}

	public static <E> boolean contains(Collection<E> collections, E element) {
		return collections.contains(element);
	}

	public static <E> boolean anyMatch(Collection<E> collections, Collection<E> allCollections) {
		return collections.stream().anyMatch(allCollections::contains);
	}

	public static <E> boolean containsAll(Collection<E> collections, Collection<E> allCollections) {
		return allCollections.containsAll(collections);
	}

}
