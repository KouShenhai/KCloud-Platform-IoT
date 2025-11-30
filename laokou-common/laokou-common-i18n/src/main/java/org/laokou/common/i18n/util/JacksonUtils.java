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

package org.laokou.common.i18n.util;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapType;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Jackson工具类.
 *
 * @author livk
 * @author laokou
 */
public final class JacksonUtils {

	/**
	 * 空JSON字符串.
	 */
	public static final String EMPTY_JSON = "{}";

	/**
	 * 映射器配置.
	 */
	private static final ObjectMapper MAPPER = getMapper();

	private JacksonUtils() {
	}

	private static ObjectMapper getMapper() {
		return JsonMapper.builder()
			.findAndAddModules()
			// 没有的属性不报错
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
			.build();
	}

	/**
	 * json字符转Bean.
	 * @param json json字符串
	 * @param clazz 类
	 * @param <T> 类型
	 * @return Bean
	 */
	public static <T> T toBean(String json, Class<T> clazz) {
		return MAPPER.readValue(json, javaType(clazz));
	}

	/**
	 * json字符转Bean.
	 * @param arr 字节数组
	 * @param clazz 类
	 * @param <T> 类型
	 * @return Bean
	 */
	public static <T> T toBean(byte[] arr, Class<T> clazz) {
		return MAPPER.readValue(arr, javaType(clazz));
	}

	public static <T> T toBean(byte[] arr, int offset, int len, Class<T> clazz) {
		return MAPPER.readValue(arr, offset, len, javaType(clazz));
	}

	/**
	 * 流转为Bean.
	 * @param inputStream 流
	 * @param clazz 类
	 * @param <T> 泛型
	 * @return Bean
	 */
	public static <T> T toBean(InputStream inputStream, Class<T> clazz) {
		return MAPPER.readValue(inputStream, javaType(clazz));
	}

	/**
	 * 序列化为json字符串.
	 * @param obj 对象
	 * @return json字符串
	 */
	public static String toJsonStr(Object obj) {
		return toJsonStr(obj, false);
	}

	/**
	 * 序列化为json字符串【序列化】.
	 * @param obj 对象
	 * @param isFormat 是否格式化
	 * @return json字符串
	 */
	public static String toJsonStr(Object obj, boolean isFormat) {
		if (obj instanceof String str) {
			return str;
		}
		if (isFormat) {
			return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		}
		return MAPPER.writeValueAsString(obj);
	}

	public static byte[] toBytes(Object obj) {
		return MAPPER.writeValueAsBytes(obj);
	}

	/**
	 * json字符串转集合对象.
	 * @param json json数组
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return 对象集合
	 */
	public static <T> List<T> toList(String json, Class<T> clazz) {
		return MAPPER.readValue(json, listType(clazz));
	}

	public static <T> List<T> toList(File file, Class<T> clazz) {
		return MAPPER.readValue(file, listType(clazz));
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
	public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.readValue(json, mapType(keyClass, valueClass));
	}

	/**
	 * Map转对象.
	 * @param obj 对象
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return 对象
	 */
	public static <T> T toValue(Object obj, Class<T> clazz) {
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
	public static <T, K, V> T toMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.convertValue(obj, mapType(keyClass, valueClass));
	}

	/**
	 * json字符串转树节点.
	 * @param json json字符串
	 * @return 树节点
	 */
	public static JsonNode readTree(String json) {
		return MAPPER.readTree(json);
	}

	/**
	 * 创建JavaType.
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return JavaType
	 */
	private static <T> JavaType javaType(Class<T> clazz) {
		return MAPPER.getTypeFactory().constructType(clazz);
	}

	/**
	 * 获取集合类型.
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return 集合类型
	 */
	private static <T> CollectionType listType(Class<T> clazz) {
		return MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
	}

	/**
	 * 创建mapType.
	 * @param keyClass 键类
	 * @param valueClass 值类
	 * @param <K> 键泛型
	 * @param <V> 值泛型
	 * @return MapType
	 */
	private static <K, V> MapType mapType(Class<K> keyClass, Class<V> valueClass) {
		return MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
	}

}
