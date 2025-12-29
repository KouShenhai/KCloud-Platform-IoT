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

package org.laokou.common.redis.util;

import org.jspecify.annotations.NonNull;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * @author laokou
 */
public record ReactiveRedisUtils(ReactiveRedisTemplate<@NonNull String, @NonNull Object> reactiveRedisTemplate,
		RedissonReactiveClient redissonReactiveClient) {

	public Mono<@NonNull Object> get(String key) {
		return redissonReactiveClient.getBucket(key).get();
	}

	public <K, V> RMapReactive<K, V> getMap(String key) {
		return redissonReactiveClient.getMap(key);
	}

	public Mono<@NonNull Object> hGet(String key, String field) {
		return redissonReactiveClient.getMap(key).get(field);
	}

	public Mono<@NonNull Boolean> hasKey(String key) {
		return reactiveRedisTemplate.hasKey(key);
	}

	public Mono<@NonNull Boolean> hasHashKey(String key, String field) {
		return redissonReactiveClient.getMap(key).containsKey(field);
	}

	public Mono<@NonNull Void> set(String key, Object obj, long expire) {
		if (expire == -1) {
			return redissonReactiveClient.getBucket(key).set(obj);
		}
		else {
			return redissonReactiveClient.getBucket(key).set(obj, Duration.ofSeconds(expire));
		}
	}

	public Mono<@NonNull Long> delete(String key) {
		return redissonReactiveClient.getKeys().delete(key);
	}

	public Flux<@NonNull Object> values(String key) {
		return redissonReactiveClient.getMap(key).valueIterator();
	}

	public Mono<@NonNull Boolean> fastPutIfAbsent(String key, String field, Object value) {
		return redissonReactiveClient.getMap(key).fastPutIfAbsent(field, value);
	}

	public Mono<@NonNull Void> putAll(String key, Map<String, Object> map) {
		return redissonReactiveClient.getMap(key).putAll(map);
	}

	public Mono<@NonNull Object> putIfAbsent(String key, String field, Object value) {
		return redissonReactiveClient.getMap(key).putIfAbsent(field, value);
	}

}
