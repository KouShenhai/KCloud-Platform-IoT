/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.laokou.common.i18n.common.Constant.AND;

/**
 * @author laokou
 */
public class MapUtil {

	public static boolean isNotEmpty(Map<?, ?> map) {
		return MapUtils.isNotEmpty(map);
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return MapUtils.isEmpty(map);
	}

	public static Map<String, String> parseParamMap(String params) {
		String[] strings = params.split(AND);
		int length = strings.length;
		if (length == 0) {
			return new HashMap<>(0);
		}
		Map<String, String> paramMap = new HashMap<>(strings.length);
		for (String string : strings) {
			int index = string.indexOf("=");
			if (index > -1) {
				String key = string.substring(0, index);
				String value = UriEncoder.decode(string.substring(index + 1));
				paramMap.put(key, value);
			}
		}
		return paramMap;
	}

	public static String parseParams(Map<String, String> paramMap, boolean isEncode) {
		if (paramMap.isEmpty()) {
			return "";
		}
		Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
		StringBuilder stringBuilder = new StringBuilder();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			stringBuilder.append(key)
				.append("=")
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
		parameterMap.forEach((key, values) -> {
			for (String value : values) {
				parameters.add(key, value);
			}
		});
		return parameters;
	}

}
