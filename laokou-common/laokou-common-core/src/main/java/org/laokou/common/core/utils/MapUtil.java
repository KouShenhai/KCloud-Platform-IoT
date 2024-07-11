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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constant.StringConstant.*;

/**
 * map工具类.
 *
 * @author laokou
 */
public class MapUtil {

	/**
	 * 判端不为空.
	 *
	 * @param map map对象
	 * @return 判断结果
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * 判断为空.
	 *
	 * @param map map对象
	 * @return 判断结果
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return ObjectUtil.isNull(map) || map.isEmpty();
	}

	/**
	 * map转字符串.
	 *
	 * @param map       map对象
	 * @param on        分隔符
	 * @param separator 分隔符
	 * @return 字符串
	 */
	public static String toStr(Map<String, String> map, String on, String separator) {
		if (map.isEmpty()) {
			return EMPTY;
		}
		return Joiner.on(on).withKeyValueSeparator(separator).join(map);
	}

	/**
	 * 转URI Map.
	 *
	 * @param uriMap    map对象
	 * @param serviceId 服务ID
	 * @param separator 分隔符
	 * @return Map对象
	 */
	public static Map<String, Set<String>> toUriMap(Map<String, Set<String>> uriMap, String serviceId,
													String separator) {
		if (uriMap.isEmpty()) {
			return new ConcurrentHashMap<>(0);
		}
		Map<String, Set<String>> maps = new ConcurrentHashMap<>(uriMap.size());
		uriMap.forEach((k, v) -> maps.put(k,
			v.stream()
				.filter(item -> item.contains(serviceId))
				.map(item -> item.substring(0, item.indexOf(separator)))
				.collect(Collectors.toSet())));
		return maps;
	}

	/**
	 * 转URI Map.
	 *
	 * @param uriMap    map对象
	 * @param serviceId 服务ID
	 * @return Map对象
	 */
	public static Map<String, Set<String>> toUriMap(Map<String, Set<String>> uriMap, String serviceId) {
		return toUriMap(uriMap, serviceId, EQUAL);
	}

	/**
	 * 字符串转为Map.
	 *
	 * @param str       字符串
	 * @param on        分隔符
	 * @param separator 分隔符
	 * @return Map对象
	 */
	public static Map<String, String> toMap(String str, String on, String separator) {
		if (StringUtil.isEmpty(str)) {
			return Collections.emptyMap();
		}
		return Splitter.on(on).trimResults().withKeyValueSeparator(separator).split(str);
	}

	/**
	 * 字符串转为param map.
	 *
	 * @param params 参数
	 * @return paramMap对象
	 */
	public static Map<String, String> parseParamMap(String params) {
		String[] strings = params.split(AND);
		int length = strings.length;
		if (length == 0) {
			return Collections.emptyMap();
		}
		Map<String, String> paramMap = new HashMap<>(strings.length);
		for (String string : strings) {
			int index = string.indexOf(EQUAL);
			if (index > -1) {
				String key = string.substring(0, index);
				if (!paramMap.containsKey(key)) {
					String value = UriEncoder.decode(string.substring(index + 1));
					paramMap.put(key, value);
				}
			}
		}
		return paramMap;
	}

	/**
	 * map转字符串.
	 *
	 * @param paramMap map对象
	 * @param isEncode 是否编码
	 * @return 字符串
	 */
	public static String parseParams(Map<String, String> paramMap, boolean isEncode) {
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
	 *
	 * @param paramMap 参数
	 * @return 字符串
	 */
	public static String parseParams(Map<String, String> paramMap) {
		return parseParams(paramMap, true);
	}

	/**
	 * 请求对象构建MultiValueMap.
	 *
	 * @param parameterMap 请求参数Map
	 * @return MultiValueMap
	 */
	public static MultiValueMap<String, String> getParameters(Map<String, String[]> parameterMap) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((k, v) -> parameters.addAll(k, Arrays.asList(v)));
		return parameters;
	}

	public static Map<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (MapUtil.isNotEmpty(parameterMap)) {
			return getParameters(parameterMap).toSingleValueMap();
		}
		byte[] requestBody = RequestUtil.getRequestBody(request);
		return ArrayUtil.isEmpty(requestBody) ? Collections.emptyMap()
			: JacksonUtil.toMap(requestBody, String.class, String.class);
	}

}
