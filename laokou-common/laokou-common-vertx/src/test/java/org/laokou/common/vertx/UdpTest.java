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
import io.vertx.core.datagram.DatagramSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UdpTest {

	private final Vertx vertx;

	@Test
	void test_udp() throws InterruptedException {
		for (int i = 4880; i < 5000; i++) {
			DatagramSocket datagramSocket = vertx.createDatagramSocket();
			int finalI = i;
			datagramSocket.send("Hello Vertx", i, "127.0.0.1").onComplete(result -> {
				if (result.succeeded()) {
					log.info("【Vertx-UDP-Client】 => 发送成功，端口：{}", finalI);
				}
				else {
					Throwable ex = result.cause();
					log.error("【Vertx-UDP-Client】 => 发送失败，端口：{}，错误信息：{}", finalI, ex.getMessage(), ex);
				}
			});
			Thread.sleep(2000);
			Assertions.assertThatNoException().isThrownBy(datagramSocket::close);
		}
	}

}
