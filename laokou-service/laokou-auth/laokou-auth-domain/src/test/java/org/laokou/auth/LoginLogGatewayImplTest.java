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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.gateway.LoginLogGateway;
import org.laokou.auth.model.LoginLogE;

/**
 * 登录日志网关测试.
 *
 * @author laokou
 */
class LoginLogGatewayImplTest implements LoginLogGateway {

	@Test
	void test() {
		LoginLogE loginLogE = new LoginLogE();
		create(loginLogE);
		Assertions.assertEquals("127.0.0.1", loginLogE.getIp());
	}

	@Override
	public void create(LoginLogE loginLog) {
		loginLog.setIp("127.0.0.1");
	}

}
