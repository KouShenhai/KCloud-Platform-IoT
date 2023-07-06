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

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
import java.util.*;

/**
 * @author livk
 * @author laokou
 */
@Slf4j
@UtilityClass
public class JacksonUtil {

	public static final String EMPTY_JSON = "{}";

	private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

	/**
	 * json字符转Bean
	 * @param json json string
	 * @param clazz class
	 * @param <T> type
	 * @return T
	 */
	@SneakyThrows
	public <T> T toBean(String json, Class<T> clazz) {
		return MAPPER.readValue(json, javaType(clazz));

	}

	/**
	 * 创建JavaType
	 * @param clazz 类型
	 * @return JavaType
	 */
	public <T> JavaType javaType(Class<T> clazz) {
		return MAPPER.getTypeFactory().constructType(clazz);
	}

	@SneakyThrows
	public <T> T toBean(InputStream inputStream, Class<T> clazz) {
		return MAPPER.readValue(inputStream, javaType(clazz));
	}

	/**
	 * 序列化 为字符串
	 * @param obj obj
	 * @return json
	 */
	@SneakyThrows
	public String toJsonStr(Object obj) {
		return toJsonStr(obj, false);
	}

	/**
	 * 序列化 为字符串
	 * @param obj obj
	 * @param isFormat 是否格式化
	 * @return json
	 */
	@SneakyThrows
	public String toJsonStr(Object obj, boolean isFormat) {
		if (obj instanceof String str) {
			return str;
		}
		if (isFormat) {
			return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		}
		return MAPPER.writeValueAsString(obj);
	}

	/**
	 * json to List
	 * @param json json数组
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return List<T>
	 */
	@SneakyThrows
	public <T> List<T> toList(String json, Class<T> clazz) {
		return MAPPER.readValue(json, collectionType(clazz));
	}

	public <T> CollectionType collectionType(Class<T> clazz) {
		return MAPPER.getTypeFactory().constructCollectionType(Collection.class, clazz);
	}

	/**
	 * json反序列化Map
	 * @param json json字符串
	 * @param keyClass K Class
	 * @param valueClass V Class
	 * @return Map<K, V>
	 */
	@SneakyThrows
	public <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.readValue(json, mapType(keyClass, valueClass));
	}

	public <K, V> MapType mapType(Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
	}

	public <T> T toValue(Object obj, Class<T> clazz) {
		return MAPPER.convertValue(obj, clazz);
	}

	@SneakyThrows
	public JsonNode readTree(String json) {
		return MAPPER.readTree(json);
	}

}
