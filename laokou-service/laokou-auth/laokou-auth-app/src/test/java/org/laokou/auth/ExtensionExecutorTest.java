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

package org.laokou.auth;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.service.extensionpoint.AuthParamValidatorExtPt;
import org.laokou.auth.service.extensionpoint.extension.UsernamePasswordAuthParamValidator;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.extension.ExtensionRepository;
import org.laokou.common.extension.register.ExtensionRegister;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

import static org.laokou.auth.model.AuthA.USE_CASE_AUTH;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * 测试扩展点执行器.
 *
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { ExtensionExecutor.class, ExtensionRepository.class, ExtensionRegister.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ExtensionExecutorTest {

	private final ExtensionExecutor extensionExecutor;

	private final ExtensionRegister extensionRegister;

	@Test
	void testUsernamePasswordAuthParamValidateExecutor() {
		Assertions.assertNotNull(extensionExecutor);
		Assertions.assertNotNull(extensionRegister);

		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertNotNull(auth);

		// 注入【用户名密码登录】校验器
		AuthParamValidatorExtPt authParamValidatorExtPt = new UsernamePasswordAuthParamValidator();
		Assertions.assertNotNull(authParamValidatorExtPt);
		extensionRegister.doRegistration(authParamValidatorExtPt);

		// 执行参数校验【用户名密码登录】
		extensionExecutor.executeVoid(AuthParamValidatorExtPt.class,
				BizScenario.valueOf(auth.getGrantType().getCode(), USE_CASE_AUTH, SCENARIO),
				extension -> extension.validate(auth));
	}

}
