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

package org.laokou.common.pulsar.config;

import org.apache.pulsar.client.impl.schema.AbstractSchema;
import org.apache.pulsar.common.schema.SchemaInfo;
import org.apache.pulsar.common.schema.SchemaType;
import org.apache.pulsar.shade.io.netty.buffer.ByteBuf;
import org.laokou.common.fory.config.ForyFactory;

/**
 * @author laokou
 */
public class ForySchema extends AbstractSchema<Object> {

	public static final ForySchema INSTANCE = new ForySchema();

	@Override
	public byte[] encode(Object message) {
		return ForyFactory.INSTANCE.serialize(message);
	}

	@Override
	public Object decode(byte[] bytes) {
		return ForyFactory.INSTANCE.deserialize(bytes);
	}

	@Override
	public SchemaInfo getSchemaInfo() {
		return SchemaInfo.builder().name("Fory").type(SchemaType.BYTES).schema(new byte[0]).build();
	}

	@Override
	public Object decode(ByteBuf byteBuf) {
		return ForyFactory.INSTANCE.deserialize(byteBuf.array());
	}

}
