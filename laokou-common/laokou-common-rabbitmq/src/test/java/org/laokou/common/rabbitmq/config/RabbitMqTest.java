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

package org.laokou.common.rabbitmq.config;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@Import({ RabbitMqConsumer.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RabbitMqTest {

	private final RabbitTemplate rabbitTemplate;

	@Test
	void test() {
		String message = "Hello, RabbitMQ!";
		rabbitTemplate.convertAndSend("laokou.fanout.exchange", "", message);
	}

}
