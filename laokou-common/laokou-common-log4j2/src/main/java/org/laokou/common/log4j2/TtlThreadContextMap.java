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

package org.laokou.common.log4j2;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @author laokou
 * @see DefaultThreadContextMap
 */
public class TtlThreadContextMap implements ThreadContextMap {

	public static final TtlThreadContextMap INSTANCE = new TtlThreadContextMap();

	private final ThreadLocal<Map<String, String>> LOCAL_MAP = TransmittableThreadLocal.withInitial(HashMap::new);

	@Override
	public void put(final String key, final String value) {
		Map<String, String> map = LOCAL_MAP.get();
		map = map == null ? new HashMap<>() : new HashMap<>(map);
		map.put(key, value);
		LOCAL_MAP.set(Collections.unmodifiableMap(map));
	}

	@Override
	public String get(final String key) {
		final Map<String, String> map = LOCAL_MAP.get();
		return map == null ? null : map.get(key);
	}

	@Override
	public void remove(final String key) {
		final Map<String, String> map = LOCAL_MAP.get();
		if (map != null) {
			final Map<String, String> copy = new HashMap<String, String>(map);
			copy.remove(key);
			LOCAL_MAP.set(Collections.unmodifiableMap(copy));
		}
	}

	@Override
	public void clear() {
		LOCAL_MAP.remove();
	}

	@Override
	public boolean containsKey(final String key) {
		final Map<String, String> map = LOCAL_MAP.get();
		return map != null && map.containsKey(key);
	}

	@Override
	public Map<String, String> getCopy() {
		final Map<String, String> map = LOCAL_MAP.get();
		return map == null ? new HashMap<>() : new HashMap<>(map);
	}

	@Override
	public Map<String, String> getImmutableMapOrNull() {
		return LOCAL_MAP.get();
	}

	@Override
	public boolean isEmpty() {
		final Map<String, String> map = LOCAL_MAP.get();
		return map == null || map.isEmpty();
	}

	@Override
	public String toString() {
		final Map<String, String> map = LOCAL_MAP.get();
		return map == null ? "{}" : map.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		final Map<String, String> map = this.LOCAL_MAP.get();
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TtlThreadContextMap other)) {
			return false;
		}
		final Map<String, String> map = this.LOCAL_MAP.get();
		final Map<String, String> otherMap = other.getImmutableMapOrNull();
		if (map == null) {
			return otherMap == null;
		}
		else
			return map.equals(otherMap);
	}

}
