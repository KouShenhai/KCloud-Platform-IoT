/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.websocket.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SpringWebSocketServerProperties unit tests.
 *
 * @author laokou
 */
class SpringWebSocketServerPropertiesTest {

	private SpringWebSocketServerProperties properties;

	@BeforeEach
	void setUp() {
		properties = new SpringWebSocketServerProperties();
	}

	@Test
	void test_default_values() {
		// Then - Verify default values
		Assertions.assertThat(properties.getIp()).isEqualTo("127.0.0.1");
		Assertions.assertThat(properties.getBindIp()).isEqualTo("0.0.0.0");
		Assertions.assertThat(properties.getPort()).isZero();
		Assertions.assertThat(properties.getMaxContentLength()).isEqualTo(65536);
		Assertions.assertThat(properties.getWebsocketPath()).isEqualTo("/ws");
		Assertions.assertThat(properties.getExplicitFlushAfterFlushes()).isEqualTo(10);
		Assertions.assertThat(properties.isConsolidateWhenNoReadInProgress()).isTrue();
		Assertions.assertThat(properties.getReaderIdleTime()).isEqualTo(60);
		Assertions.assertThat(properties.getWriterIdleTime()).isZero();
		Assertions.assertThat(properties.getAllIdleTime()).isZero();
		Assertions.assertThat(properties.getBossCorePoolSize()).isEqualTo(2);
		Assertions.assertThat(properties.getWorkerCorePoolSize()).isEqualTo(40);
		Assertions.assertThat(properties.isTcpNodelay()).isFalse();
		Assertions.assertThat(properties.getBacklogLength()).isEqualTo(1024);
		Assertions.assertThat(properties.isKeepAlive()).isTrue();
		Assertions.assertThat(properties.getMaxHeartBeatCount()).isEqualTo(60);
		Assertions.assertThat(properties.getKeyCertPrivateKeyPassword()).isEqualTo("laokou");
		Assertions.assertThat(properties.getKeyCertChainPath()).isEqualTo("classpath:certificate.crt");
		Assertions.assertThat(properties.getKeyCertPrivateKeyPath()).isEqualTo("classpath:private.key");
		Assertions.assertThat(properties.isUseIoUring()).isTrue();
	}

	@Test
	void test_setter_getter() {
		// When
		properties.setIp("192.168.1.100");
		properties.setBindIp("192.168.1.0");
		properties.setPort(9090);
		properties.setServiceId("websocket-service");
		properties.setMaxContentLength(131072);
		properties.setWebsocketPath("/websocket");
		properties.setExplicitFlushAfterFlushes(20);
		properties.setConsolidateWhenNoReadInProgress(false);
		properties.setReaderIdleTime(120);
		properties.setWriterIdleTime(30);
		properties.setAllIdleTime(180);
		properties.setBossCorePoolSize(4);
		properties.setWorkerCorePoolSize(80);
		properties.setTcpNodelay(true);
		properties.setBacklogLength(2048);
		properties.setKeepAlive(false);
		properties.setMaxHeartBeatCount(30);
		properties.setKeyCertPrivateKeyPassword("newpassword");
		properties.setKeyCertChainPath("classpath:new-certificate.crt");
		properties.setKeyCertPrivateKeyPath("classpath:new-private.key");
		properties.setUseIoUring(false);

		// Then
		Assertions.assertThat(properties.getIp()).isEqualTo("192.168.1.100");
		Assertions.assertThat(properties.getBindIp()).isEqualTo("192.168.1.0");
		Assertions.assertThat(properties.getPort()).isEqualTo(9090);
		Assertions.assertThat(properties.getServiceId()).isEqualTo("websocket-service");
		Assertions.assertThat(properties.getMaxContentLength()).isEqualTo(131072);
		Assertions.assertThat(properties.getWebsocketPath()).isEqualTo("/websocket");
		Assertions.assertThat(properties.getExplicitFlushAfterFlushes()).isEqualTo(20);
		Assertions.assertThat(properties.isConsolidateWhenNoReadInProgress()).isFalse();
		Assertions.assertThat(properties.getReaderIdleTime()).isEqualTo(120);
		Assertions.assertThat(properties.getWriterIdleTime()).isEqualTo(30);
		Assertions.assertThat(properties.getAllIdleTime()).isEqualTo(180);
		Assertions.assertThat(properties.getBossCorePoolSize()).isEqualTo(4);
		Assertions.assertThat(properties.getWorkerCorePoolSize()).isEqualTo(80);
		Assertions.assertThat(properties.isTcpNodelay()).isTrue();
		Assertions.assertThat(properties.getBacklogLength()).isEqualTo(2048);
		Assertions.assertThat(properties.isKeepAlive()).isFalse();
		Assertions.assertThat(properties.getMaxHeartBeatCount()).isEqualTo(30);
		Assertions.assertThat(properties.getKeyCertPrivateKeyPassword()).isEqualTo("newpassword");
		Assertions.assertThat(properties.getKeyCertChainPath()).isEqualTo("classpath:new-certificate.crt");
		Assertions.assertThat(properties.getKeyCertPrivateKeyPath()).isEqualTo("classpath:new-private.key");
		Assertions.assertThat(properties.isUseIoUring()).isFalse();
	}

}
