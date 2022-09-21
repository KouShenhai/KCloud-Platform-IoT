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
package io.laokou.redis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.TimeUnit;
/**
 * Redis工具类
 * @author  Kou Shenhai
 */
@Component
public final class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**  默认过期时长为24小时，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**  过期时长为1小时，单位：秒 */
    public final static long HOUR_ONE_EXPIRE = 60 * 60 * 1L;
    /**  过期时长为6小时，单位：秒 */
    public final static long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1L;
    public final void set(String key, String value, long expire){
        redisTemplate.opsForValue().set(key, value);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public final void set(String key, String value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public final Object get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
        return value;
    }

    public final Object get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public final void delete(String key) {
        redisTemplate.delete(key);
    }

    public final void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public final Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public final Map<String, Object> hGetAll(String key){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public final void hMSet(String key, Map<String, Object> map){
        hMSet(key, map, DEFAULT_EXPIRE);
    }

    public final void hMSet(String key, Map<String, Object> map, long expire){
        redisTemplate.opsForHash().putAll(key, map);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final void hSet(String key, String field, String value) {
        hSet(key, field, value, DEFAULT_EXPIRE);
    }

    public final void hSet(String key, String field, String value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final void expire(String key, long expire){
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public final void hDel(String key, Object... fields){
        redisTemplate.opsForHash().delete(key, fields);
    }

    public final void leftPush(String key, String value){
        leftPush(key, value, DEFAULT_EXPIRE);
    }

    public final void leftPush(String key, String value, long expire){
        redisTemplate.opsForList().leftPush(key, value);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final Object rightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    public final Long getKeysSize() {
        final Object obj = redisTemplate.execute((RedisCallback) connection -> connection.dbSize());
        return obj == null ? 0 : Long.valueOf(obj.toString());
    }

    public final List<Map<String, String>> getCommandStatus() {
        final Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        return pieList;
    }

    public final Map<String, String> getInfo() {
        final Properties properties = (Properties) redisTemplate.execute((RedisCallback) connection -> connection.info());
        final Set<String> set = properties.stringPropertyNames();
        final Iterator<String> iterator = set.iterator();
        Map<String,String> dataMap = new HashMap<>(set.size());
        while (iterator.hasNext()) {
            final String key = iterator.next();
            final String value = properties.getProperty(key);
            dataMap.put(key, value);
        }
        return dataMap;
    }

}
