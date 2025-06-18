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

package org.laokou.common.kafka.config;

import org.apache.kafka.common.serialization.Serializer;
import org.laokou.common.fory.config.ForyFactory;

/**
 * @author laokou
 */
public class ForyKafkaSerializer implements Serializer<Object> {

	@Override
	public byte[] serialize(String s, Object o) {
		return ForyFactory.INSTANCE.serialize(o);
	}

}
