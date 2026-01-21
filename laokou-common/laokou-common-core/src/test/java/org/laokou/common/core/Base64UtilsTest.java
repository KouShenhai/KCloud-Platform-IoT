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

package org.laokou.common.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.Base64Utils;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
class Base64UtilsTest {

	@Test
	void test_encodeAndDecode_withValidInput_returnsOriginalBytes() {
		String name = "zzzzzzzzzzzccccc\nccccccccccccccccccc231231qweqweqw";
		String encodeToString = Base64Utils.encodeToString(name.getBytes(StandardCharsets.UTF_8));
		byte[] decode = Base64Utils.decode(encodeToString);
		Assertions.assertThat(decode).isEqualTo(name.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void test_encodeAndDecodeOfMime_withValidInput_returnsOriginalBytes() {
		String name = "zzzzzzzzzzzccccc\nccccccccccccccccccc231231qweqweqw";
		String encodeToStringOfMime = Base64Utils.encodeToStringOfMime(name.getBytes(StandardCharsets.UTF_8));
		byte[] mimeDecode = Base64Utils.decodeOfMime(encodeToStringOfMime);
		Assertions.assertThat(mimeDecode).isEqualTo(name.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void test_encode_withEmptyInput_returnsEmptyString() {
		byte[] emptyBytes = new byte[0];
		String encoded = Base64Utils.encodeToString(emptyBytes);
		Assertions.assertThat(encoded).isEmpty();
		byte[] decoded = Base64Utils.decode(encoded);
		Assertions.assertThat(decoded).isEmpty();
	}

	@Test
	void test_decode_withInvalidBase64_throwsException() {
		Assertions.assertThatThrownBy(() -> Base64Utils.decode("!@#$%^&*()"))
				.isInstanceOf(IllegalArgumentException.class);
	}

}
