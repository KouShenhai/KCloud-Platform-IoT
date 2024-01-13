/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.StringConstants.*;

/**
 * @author laokou
 */
public class MapUtil {

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return ObjectUtil.isNull(map) || map.isEmpty();
	}

	public static String toStr(Map<String, String> map, String on, String separator) {
		if (map.isEmpty()) {
			return EMPTY;
		}
		return Joiner.on(on).withKeyValueSeparator(separator).join(map);
	}

	public static Map<String, Set<String>> toUriMap(Map<String, Set<String>> uriMap, String serviceId,
			String separator) {
		if (uriMap.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<String, Set<String>> maps = new HashMap<>(uriMap.size());
		uriMap.forEach((k, v) -> maps.put(k,
				v.stream()
					.filter(item -> item.contains(serviceId))
					.map(item -> item.substring(0, item.indexOf(separator)))
					.collect(Collectors.toSet())));
		return maps;
	}

	public static Map<String, Set<String>> toUriMap(Map<String, Set<String>> uriMap, String serviceId) {
		return toUriMap(uriMap, serviceId, EQUAL);
	}

	public static Map<String, String> toMap(String str, String on, String separator) {
		if (StringUtil.isEmpty(str)) {
			return new HashMap<>(0);
		}
		return Splitter.on(on).trimResults().withKeyValueSeparator(separator).split(str);
	}

	public static Map<String, String> parseParamMap(String params) {
		String[] strings = params.split(AND);
		int length = strings.length;
		if (length == 0) {
			return new HashMap<>(0);
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

	public static String parseParams(Map<String, String> paramMap) {
		return parseParams(paramMap, true);
	}

	public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((k, v) -> parameters.addAll(k, Arrays.asList(v)));
		return parameters;
	}

}
