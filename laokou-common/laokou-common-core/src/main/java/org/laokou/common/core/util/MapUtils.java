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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constant.StringConstants.*;

/**
 * map工具类.
 *
 * @author laokou
 */
public final class MapUtils {

	private MapUtils() {
	}

	/**
	 * 根据负载因子【0.75】计算初始化容量.
	 * @param size 容量
	 */
	public static int initialCapacity(int size) {
		return Math.ceilDiv(size * 100, 75);
	}

	/**
	 * 判断不为空.
	 * @param map map对象
	 * @return 判断结果
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * 判断为空.
	 * @param map map对象
	 * @return 判断结果
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return ObjectUtils.isNull(map) || map.isEmpty();
	}

	/**
	 * 转URI Map.
	 * @param uriMap map对象
	 * @param serviceId 服务ID
	 * @param separator 分隔符
	 * @return Map对象
	 */
	public static Map<String, Set<String>> toUriMap(Map<String, Set<String>> uriMap, String serviceId,
			String separator) {
		return uriMap.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
					entry -> entry.getValue()
						.stream()
						.filter(item -> item.contains(serviceId))
						.map(item -> item.substring(0, item.indexOf(separator)))
						.collect(Collectors.toSet())));
	}

	/**
	 * 转URI Map.
	 * @param uriMap map对象
	 * @param serviceId 服务ID
	 * @return Map对象
	 */
	public static Map<String, Set<String>> toUriMap(Map<String, Set<String>> uriMap, String serviceId) {
		return toUriMap(uriMap, serviceId, EQUAL);
	}

	// @formatter:off
	/**
	 * 字符串参数转为map参数.
	 * @see <a href="https://github.com/livk-cloud/spring-boot-extension/blob/main/spring-extension-commons/src/main/java/com/livk/commons/util/MultiValueMapSplitter.java">MultiValueMapSplitter</a>.
	 * @param params 参数
	 * @return map参数对象
	 */
	public static MultiValueMap<String, String> getParameterMap(String params, String separator) {
		if (StringUtils.isNotEmpty(params)) {
			String[] strings = params.split(separator);
			MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>(strings.length * 2);
			for (String string : strings) {
				int index = string.indexOf(EQUAL);
				if (index > -1) {
					String key = string.substring(0, index);
					String value = UriEncoder.decode(string.substring(index + 1));
					parameterMap.add(key, value);
				}
			}
			return parameterMap;
		}
		return new LinkedMultiValueMap<>(0);
	}
	// @formatter:on

	/**
	 * map转字符串.
	 * @param paramMap map对象
	 * @param isEncode 是否编码
	 * @return 字符串
	 */
	public static String parseParamterString(Map<String, String> paramMap, boolean isEncode) {
		if (paramMap.isEmpty()) {
			return EMPTY;
		}
		Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
		StringBuilder stringBuilder = new StringBuilder();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			stringBuilder.append(key)
				.append(EQUAL)
				.append(isEncode ? URLEncoder.encode(value, StandardCharsets.UTF_8) : value)
				.append(AND);
		}
		String str = stringBuilder.toString();
		return str.substring(0, str.length() - 1);
	}

	/**
	 * map转为字符串.
	 * @param paramMap 参数
	 * @return 字符串
	 */
	public static String parseParamterString(Map<String, String> paramMap) {
		return parseParamterString(paramMap, true);
	}

	/**
	 * 请求对象构建MultiValueMap.
	 * @param parameterMap 请求参数Map
	 * @return MultiValueMap
	 */
	public static MultiValueMap<String, String> getParameterMap(Map<String, String[]> parameterMap) {
		Map<String, List<String>> transformValues = Maps.transformValues(parameterMap, Lists::newArrayList);
		return new MultiValueMapAdapter<>(transformValues);
	}

}
