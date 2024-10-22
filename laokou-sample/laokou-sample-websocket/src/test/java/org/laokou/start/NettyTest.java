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

package org.laokou.start;

import io.netty.channel.ChannelHandler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.ClassUtil;
import org.laokou.common.core.utils.SpringContextUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NettyTest {

	@Test
	@SneakyThrows
	void test() {
		Class<?> clazz = ClassUtil.parseClass("org.laokou.app.initializer.WebSocketServerChannelInitializer");
		Object obj = SpringContextUtil.getBean(clazz);
		if (obj instanceof ChannelHandler channelHandler) {
			Assertions.assertNotNull(channelHandler);
		}
	}

}
