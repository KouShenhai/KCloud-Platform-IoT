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

package org.laokou.common.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.net.ConnectOptions;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class TcpTest {

	private final Vertx vertx;

	@Test
	void test() throws InterruptedException {
		for (int i = 3880; i <= 4000; i++) {
			int finalI = i;
			ConnectOptions connectOptions = new ConnectOptions();
			connectOptions.setPort(finalI);
			connectOptions.setHost("127.0.0.1");
			NetClientOptions options = new NetClientOptions().setConnectTimeout(10000);
			NetClient netClient = vertx.createNetClient(options);
			netClient.connect(connectOptions).onComplete(res -> {
				if (res.succeeded()) {
					log.info("【Vertx-TCP-Client】 => 连接成功，端口：{}", finalI);
					assertThatNoException().isThrownBy(() -> res.result().write("发送数据，" + finalI + " ---> 123"));
				}
				else {
					log.info("【Vertx-TCP-Client】 => 连接失败，端口：{}", finalI);
				}
			});
			Thread.sleep(2000);
			assertThatNoException().isThrownBy(netClient::close);
		}
	}

}
