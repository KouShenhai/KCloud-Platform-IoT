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

package org.laokou.admin;

import io.seata.saga.engine.StateMachineEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class SeataSagaTest extends CommonTest {

	private final StateMachineEngine stateMachineEngine;

	public SeataSagaTest(WebApplicationContext webApplicationContext,
			OAuth2AuthorizationService oAuth2AuthorizationService, StateMachineEngine stateMachineEngine) {
		super(webApplicationContext, oAuth2AuthorizationService);
		this.stateMachineEngine = stateMachineEngine;
	}

	@Test
	public void testStateMachineEngine() {
		stateMachineEngine.start(EMPTY, "0", Collections.emptyMap());
	}

}
