/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Jackson工具类.
 *
 * @author livk
 * @author laokou
 */
@Slf4j
@UtilityClass
public class JacksonUtil {

	@Schema(name = "EMPTY_JSON", description = "空JSON字符串")
	public static final String EMPTY_JSON = "{}";

	/**
	 * 映射器配置.
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

	/**
	 * json字符转Bean.
	 * @param json json json字符串
	 * @param clazz class 类
	 * @param <T> type 类型
	 * @return Bean
	 */
	@SneakyThrows
	public <T> T toBean(String json, Class<T> clazz) {
		return MAPPER.readValue(json, javaType(clazz));

	}

	/**
	 * 创建JavaType.
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return JavaType
	 */
	public <T> JavaType javaType(Class<T> clazz) {
		return MAPPER.getTypeFactory().constructType(clazz);
	}

	/**
	 * 流转为Bean.
	 * @param inputStream 流
	 * @param clazz 类
	 * @param <T> 泛型
	 * @return Bean
	 */
	@SneakyThrows
	public <T> T toBean(InputStream inputStream, Class<T> clazz) {
		return MAPPER.readValue(inputStream, javaType(clazz));
	}

	/**
	 * 序列化为json字符串.
	 * @param obj 对象
	 * @return json字符串
	 */
	@SneakyThrows
	public String toJsonStr(Object obj) {
		return toJsonStr(obj, false);
	}

	/**
	 * 序列化为字符串.
	 * @param obj 对象
	 * @param isFormat 是否格式化
	 * @return json字符串
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
	 * json字符串转对象集合.
	 * @param json json数组
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return 对象集合
	 */
	@SneakyThrows
	public <T> List<T> toList(String json, Class<T> clazz) {
		return MAPPER.readValue(json, collectionType(clazz));
	}

	public <T> CollectionType collectionType(Class<T> clazz) {
		return MAPPER.getTypeFactory().constructCollectionType(Collection.class, clazz);
	}

	/**
	 * Json反序列化Map.
	 * @param json json字符串
	 * @param keyClass K 键类
	 * @param valueClass V 值类
	 * @param <K> 键泛型
	 * @param <V> 值泛型
	 * @return map
	 */
	@SneakyThrows
	public <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.readValue(json, mapType(keyClass, valueClass));
	}

	/**
	 * 创建mapType.
	 * @param keyClass 键类
	 * @param valueClass 值类
	 * @param <K> 键泛型
	 * @param <V> 值泛型
	 * @return MapType
	 */
	public <K, V> MapType mapType(Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
	}

	/**
	 * Map转对象.
	 * @param obj 对象
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return 对象
	 */
	public <T> T toValue(Object obj, Class<T> clazz) {
		return MAPPER.convertValue(obj, clazz);
	}

	/**
	 * Map转对象.
	 * @param obj 对象
	 * @param keyClass 键类
	 * @param valueClass 值类
	 * @param <K> 键泛型
	 * @param <T> 泛型
	 * @param <V> 值泛型
	 * @return 对象
	 */
	public <T, K, V> T toMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.convertValue(obj, mapType(keyClass, valueClass));
	}

	/**
	 * json字符串转树节点.
	 * @param json json字符串
	 * @return 树节点
	 */
	@SneakyThrows
	public JsonNode readTree(String json) {
		return MAPPER.readTree(json);
	}

}
