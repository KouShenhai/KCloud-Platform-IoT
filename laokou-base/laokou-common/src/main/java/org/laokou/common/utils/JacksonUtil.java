/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.*;


@Slf4j
@UtilityClass
public class JacksonUtil {

    /**
     * The constant JSON_EMPTY.
     */
    public static final String JSON_EMPTY = "{}";

    private static final ObjectMapper MAPPER = JsonMapper.builder().build();

    /**
     * json字符转Bean
     *
     * @param json  json string
     * @param clazz class
     * @param <T>   type
     * @return T
     */
    @SneakyThrows
    public static <T> T toBean(String json, Class<T> clazz) {
        if (check(json, clazz)) {
            return null;
        }
        if (clazz.isInstance(json)) {
            return (T) json;
        }
        return MAPPER.readValue(json, clazz);

    }

    @SneakyThrows
    public static <T> T toBean(InputStream inputStream, Class<T> clazz) {
        return (inputStream == null || clazz == null) ? null :
                MAPPER.readValue(inputStream, clazz);
    }

    /**
     * 序列化
     *
     * @param obj obj
     * @return json
     */
    @SneakyThrows
    public static String toJsonStr(Object obj) {
        return toJsonStr(obj,false);
    }

    /**
     * 序列化
     *
     * @param obj obj
     * @param flag 是否格式化
     * @return json
     */
    @SneakyThrows
    public static String toJsonStr(Object obj,boolean flag) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (flag) {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
        return MAPPER.writeValueAsString(obj);
    }

    /**
     * json to List
     *
     * @param json  json数组
     * @param clazz 类型
     * @param <T>   泛型
     * @return List<T>
     */
    @SneakyThrows
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (check(json, clazz)) {
            return new ArrayList<>();
        }
        CollectionType collectionType = MAPPER.getTypeFactory()
                .constructCollectionType(List.class, clazz);
        return MAPPER.readValue(json, collectionType);
    }

    /**
     * json反序列化Map
     *
     * @param json       json字符串
     * @param keyClass   K Class
     * @param valueClass V Class
     * @return Map<K, V>
     */
    @SneakyThrows
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (check(json, keyClass, valueClass)) {
            return Collections.emptyMap();
        }
        MapType mapType = MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return MAPPER.readValue(json, mapType);
    }

    @SneakyThrows
    public Properties toProperties(InputStream inputStream) {
        if (inputStream == null) {
            return new Properties();
        }
        JavaType javaType = MAPPER.getTypeFactory().constructType(Properties.class);
        return MAPPER.readValue(inputStream, javaType);
    }

    @SneakyThrows
    public <T> T toBean(String json, TypeReference<T> typeReference) {
        return check(json, typeReference) ? null :
                MAPPER.readValue(json, typeReference);
    }

    @SneakyThrows
    public JsonNode readTree(String json) {
        return MAPPER.readTree(json);
    }

    private boolean check(String json, Object... checkObj) {
        return json == null || json.isEmpty() || ObjectUtil.anyChecked(Objects::isNull, checkObj);
    }
}
