/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.apache.logging.log4j.internal.map.UnmodifiableArrayBackedMap;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author log4j
 * @author laokou
 * @see DefaultThreadContextMap
 */
public class TtlThreadContextMap implements ThreadContextMap, ReadOnlyStringMap {

	private final ThreadLocal<Object[]> localState;

	public TtlThreadContextMap() {
		localState = new TransmittableThreadLocal<>();
	}

	@Override
	public void put(final String key, final String value) {
		final Object[] state = localState.get();
		if (state == null || state.length == 0) {
			// Initialize with new entry when state is null or empty
			localState.set(UnmodifiableArrayBackedMap.EMPTY_MAP.copyAndPut(key, value).getBackingArray());
		}
		else {
			localState.set(UnmodifiableArrayBackedMap.getMap(state).copyAndPut(key, value).getBackingArray());
		}
	}

	@Override
	public String get(final String key) {
		final Object[] state = localState.get();
		if (state == null || state.length == 0) {
			return null;
		}
		return UnmodifiableArrayBackedMap.getMap(state).get(key);
	}

	@Override
	public void remove(final String key) {
		final Object[] state = localState.get();
		if (state != null && state.length > 0) {
			localState.set(UnmodifiableArrayBackedMap.getMap(state).copyAndRemove(key).getBackingArray());
		}
	}

	/**
	 * @since 2.8
	 */
	public void removeAll(final Iterable<String> keys) {
		final Object[] state = localState.get();
		if (state != null && state.length > 0) {
			localState.set(UnmodifiableArrayBackedMap.getMap(state).copyAndRemoveAll(keys).getBackingArray());
		}
	}

	@Override
	public void clear() {
		localState.remove();
	}

	@Override
	public Map<String, String> toMap() {
		return getCopy();
	}

	@Override
	public boolean containsKey(final String key) {
		final Object[] state = localState.get();
		if (state == null || state.length == 0) {
			return false;
		}
		return UnmodifiableArrayBackedMap.getMap(state).containsKey(key);
	}

	@Override
	public <V> void forEach(final BiConsumer<String, ? super V> action) {
		final Object[] state = localState.get();
		if (state == null || state.length == 0) {
			return;
		}
		UnmodifiableArrayBackedMap.getMap(state).forEach(action);
	}

	@Override
	public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
		final Object[] localState = this.localState.get();
		if (localState == null || localState.length == 0) {
			return;
		}
		UnmodifiableArrayBackedMap.getMap(localState).forEach(action, state);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue(final String key) {
		return (V) get(key);
	}

	/**
	 * {@return a mutable copy of the current thread context map}
	 */
	@Override
	public Map<String, String> getCopy() {
		final Object[] state = localState.get();
		if (state == null || state.length == 0) {
			return new HashMap<>(0);
		}

		final Map<String, String> map = UnmodifiableArrayBackedMap.getMap(state);

		// Handle empty map case efficiently - constructor is faster for empty maps
		if (map.isEmpty()) {
			return new HashMap<>();
		}

		// Pre-size HashMap to minimize rehashing operations
		// Factor 1.35 accounts for HashMap's 0.75 load factor (1/0.75 ≈ 1.33)
		return getStringStringHashMap(map);
	}

	private static HashMap<String, String> getStringStringHashMap(Map<String, String> map) {
		final HashMap<String, String> copy = new HashMap<>((int) (map.size() * 1.35));

		// Manual iteration avoids megamorphic virtual calls that prevent JIT
		// optimization.
		// The HashMap(Map) constructor requires (3 + 4n) virtual method calls that
		// become
		// megamorphic when used with different map types, leading to 24-136%
		// performance
		// degradation. Manual iteration creates monomorphic call sites that JIT can
		// optimize.
		// See https://bugs.openjdk.org/browse/JDK-8368292
		copy.putAll(map);
		return copy;
	}

	@Override
	public Map<String, String> getImmutableMapOrNull() {
		final Object[] state = localState.get();
		if (state == null || state.length == 0) {
			return null;
		}
		return UnmodifiableArrayBackedMap.getMap(state);
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		final Object[] state = localState.get();
		if (state == null || state.length == 0) {
			return 0;
		}
		return UnmodifiableArrayBackedMap.getMap(state).size();
	}

	@Override
	public String toString() {
		final Object[] state = localState.get();
		return state == null ? "{}" : UnmodifiableArrayBackedMap.getMap(state).toString();
	}

	@Override
	public int hashCode() {
		return toMap().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof ReadOnlyStringMap readOnlyStringMap) {
			if (size() != readOnlyStringMap.size()) {
				return false;
			}

			// Convert to maps and compare
			obj = readOnlyStringMap.toMap();
		}
		if (!(obj instanceof ThreadContextMap other)) {
			return false;
		}
		final Map<String, String> map = UnmodifiableArrayBackedMap.getMap(localState.get());
		final Map<String, String> otherMap = other.getImmutableMapOrNull();
		return Objects.equals(map, otherMap);
	}

}
