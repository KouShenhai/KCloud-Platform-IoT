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

package org.laokou.common.crypto.annotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.io.IOException;

/**
 * @author laokou
 */
@NoArgsConstructor
@AllArgsConstructor
public class CryptoSerializer extends JsonSerializer<String> implements ContextualSerializer {

	private CryptoType cryptoType;

	private boolean isEncrypt;

	@Override
	public void serialize(String str, JsonGenerator generator, SerializerProvider provider) throws IOException {
		if (isEncrypt) {
			generator.writeString(cryptoType.encrypt(str));
		}
		else {
			generator.writeString(cryptoType.decrypt(str));
		}
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty beanProperty) {
		JacksonCrypto jacksonCrypto = beanProperty.getAnnotation(JacksonCrypto.class);
		if (ObjectUtil.isNotNull(jacksonCrypto)) {
			return new CryptoSerializer(jacksonCrypto.type(), jacksonCrypto.isEncrypt());
		}
		throw new RuntimeException();
	}

}
