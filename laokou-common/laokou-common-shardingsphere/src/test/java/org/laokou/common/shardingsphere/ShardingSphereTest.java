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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.shardingsphere;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ShardingSphereTest {

	private final TestUserMapper testUserMapper;

	@Test
	void test() throws Exception {
		String username = "root";
		String password = "laokou123";
		String encryptUsername = CryptoUtils.encrypt(username);
		String encryptPassword = CryptoUtils.encrypt(password);
		log.info("用户名加密后：{}", encryptUsername);
		log.info("密码加密后：{}", encryptPassword);
		String decryptUsername = CryptoUtils.decrypt(encryptUsername);
		String decryptPassword = CryptoUtils.decrypt(encryptPassword);
		log.info("用户名解密后：{}", decryptUsername);
		log.info("密码解密后：{}", decryptPassword);
		Assertions.assertEquals(username, decryptUsername);
		Assertions.assertEquals(password, decryptPassword);
		List<TestUserDO> list = testUserMapper.selectList(Wrappers.emptyWrapper());
		Assertions.assertFalse(list.isEmpty());
		Assertions.assertTrue(list.stream()
			.map(TestUserDO::getName)
			.collect(Collectors.joining(","))
			.contains("boot_sys_user_202410"));
	}

}
