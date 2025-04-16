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

package org.laokou.common.domain.config;

import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.CompatibleMode;
import org.apache.fury.config.Language;

/**
 * @author laokou
 */
public final class RocketMQFuryFactory {

	private static final RocketMQFuryFactory FACTORY = new RocketMQFuryFactory();

	private final ThreadSafeFury FURY = Fury.builder()
		.withLanguage(Language.JAVA)
		// enable reference tracking for shared/circular reference.
		// Disable it will have better performance if no duplicate reference.
		.withRefTracking(false)
		// compress int for smaller size
		// .withIntCompressed(true)
		// compress long for smaller size
		// .withLongCompressed(true)
		.withCompatibleMode(CompatibleMode.SCHEMA_CONSISTENT)
		// enable type forward/backward compatibility
		// disable it for small size and better performance.
		// .withCompatibleMode(CompatibleMode.COMPATIBLE)
		// enable async multi-threaded compilation.
		.withAsyncCompilation(true)
		.requireClassRegistration(true)
		.buildThreadSafeFury();

	private RocketMQFuryFactory() {
		FURY.register(org.laokou.common.i18n.dto.DomainEvent.class);
	}

	public static RocketMQFuryFactory getFuryFactory() {
		return FACTORY;
	}

	public byte[] serialize(Object object) {
		if (object == null) {
			return new byte[0];
		}
		return FURY.serialize(object);
	}

	public Object deserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return FURY.deserialize(bytes);
	}

}
