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

import org.laokou.common.crypto.utils.AESUtils;
import org.laokou.common.crypto.utils.RSAUtils;

public enum CipherType {

	AES {
		@Override
		public String decrypt(String str) throws Exception {
			return AESUtils.decrypt(str);
		}

		@Override
		public String encrypt(String str) throws Exception {
			return AESUtils.encrypt(str);
		}
	},
	RSA {
		@Override
		public String decrypt(String str) {
			return RSAUtils.decryptByPrivateKey(str);
		}

		@Override
		public String encrypt(String str) {
			return RSAUtils.encryptByPublicKey(str);
		}
	};

	public abstract String decrypt(String str) throws Exception;

	public abstract String encrypt(String str) throws Exception;

}
