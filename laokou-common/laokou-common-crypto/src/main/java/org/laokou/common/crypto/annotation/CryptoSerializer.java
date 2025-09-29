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

package org.laokou.common.crypto.annotation;

import tools.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class CryptoSerializer extends JsonSerializer<String> implements ContextualSerializer {

	private final CipherType cipherType;

	private final boolean isEncrypt;

	@Override
	public void serialize(String str, JsonGenerator generator, SerializerProvider provider) {
		try {
			if (isEncrypt) {
				generator.writeString(cipherType.encrypt(str));
			}
			else {
				generator.writeString(cipherType.decrypt(str));
			}
		}
		catch (Exception e) {
			log.error("加密/解密失败，错误信息：{}", e.getMessage(), e);
		}
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty beanProperty) {
		Cipher cipher = beanProperty.getAnnotation(Cipher.class);
		if (ObjectUtils.isNotNull(cipher)) {
			return new CryptoSerializer(cipher.type(), cipher.isEncrypt());
		}
		throw new RuntimeException();
	}

}
