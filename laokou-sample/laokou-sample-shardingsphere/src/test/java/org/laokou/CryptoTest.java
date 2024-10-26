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

package org.laokou;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author laokou
 */
@Slf4j
class CryptoTest {

	@Test
	@SneakyThrows
	void testRSA() {
		String username = "root";
		String password = "laokou123";
		String encryptUsername = CryptoUtils.encrypt(username);
		String encryptPassword = CryptoUtils.encrypt(password);
		log.info("用户名加密后：{}", encryptUsername);
		log.info("密码加密后：{}", encryptPassword);
		String decryptUsername = CryptoUtils.decrypt(encryptUsername);
		String decryptPassword = CryptoUtils.decrypt(encryptPassword);
		Assertions.assertEquals(decryptUsername, username);
		Assertions.assertEquals(decryptPassword, password);
	}

}
