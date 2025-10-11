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

package org.laokou.common.log4j2.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @author laokou
 * @see DefaultThreadContextMap
 */
@Data
public class TtlThreadContextMap implements ThreadContextMap {

	public static final TtlThreadContextMap INSTANCE = new TtlThreadContextMap();

	private final ThreadLocal<Map<String, String>> LOCAL_MAP = TransmittableThreadLocal.withInitial(HashMap::new);

	@Override
	public void put(final String key, final String value) {
		Optional.ofNullable(LOCAL_MAP.get()).ifPresent(map -> {
			map.put(key, value);
			LOCAL_MAP.set(Collections.unmodifiableMap(map));
		});
	}

	@Override
	public String get(final String key) {
		return Optional.ofNullable(LOCAL_MAP.get()).orElseGet(HashMap::new).get(key);
	}

	@Override
	public void remove(final String key) {
		Optional.ofNullable(LOCAL_MAP.get()).ifPresent(map -> {
			map.remove(key);
			LOCAL_MAP.set(Collections.unmodifiableMap(map));
		});
	}

	@Override
	public void clear() {
		LOCAL_MAP.remove();
	}

	@Override
	public boolean containsKey(final String key) {
		return Optional.ofNullable(LOCAL_MAP.get()).orElseGet(HashMap::new).containsKey(key);
	}

	@Override
	public Map<String, String> getCopy() {
		return Optional.ofNullable(LOCAL_MAP.get()).orElseGet(HashMap::new);
	}

	@Override
	public Map<String, String> getImmutableMapOrNull() {
		return LOCAL_MAP.get();
	}

	@Override
	public boolean isEmpty() {
		return Optional.ofNullable(LOCAL_MAP.get()).orElseGet(HashMap::new).isEmpty();
	}

}
