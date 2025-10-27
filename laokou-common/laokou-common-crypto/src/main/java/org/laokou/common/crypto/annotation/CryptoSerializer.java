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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class CryptoSerializer extends ValueSerializer<String> {

	private final CipherType cipherType;

	private final boolean isEncrypt;

	@Override
	public void serialize(String value, tools.jackson.core.JsonGenerator generator, SerializationContext context)
			throws JacksonException {
		try {
			if (isEncrypt) {
				generator.writeString(cipherType.encrypt(value));
			}
			else {
				generator.writeString(cipherType.decrypt(value));
			}
		}
		catch (Exception e) {
			log.error("加密/解密失败，错误信息：{}", e.getMessage(), e);
		}
	}

}
