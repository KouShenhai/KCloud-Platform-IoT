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

package org.laokou.network.connection.ability;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.network.connection.gateway.ConnectionGateway;
import org.laokou.network.connection.model.ConnectionE;
import org.mockito.Mockito;

/**
 * Network connection domain service tests.
 *
 * @author laokou
 */
class ConnectionDomainServiceTest {

	private ConnectionE newValidConnection() {
		ConnectionE connectionE = new ConnectionE();
		connectionE.setName("MQTT Server");
		connectionE.setType(1);
		connectionE.setHost("127.0.0.1");
		connectionE.setPort(1883);
		connectionE.setEnabled(0);
		connectionE.setConfig("{}");
		connectionE.setRemark("test");
		return connectionE;
	}

	@Test
	@DisplayName("create connection success")
	void createConnection_success() {
		ConnectionGateway gateway = Mockito.mock(ConnectionGateway.class);
		ConnectionDomainService service = new ConnectionDomainService(gateway);

		Assertions.assertThatCode(() -> service.createConnection(newValidConnection())).doesNotThrowAnyException();
		Mockito.verify(gateway).createConnection(Mockito.any(ConnectionE.class));
	}

	@Test
	@DisplayName("create connection with blank name fails")
	void createConnection_blankName_fail() {
		ConnectionGateway gateway = Mockito.mock(ConnectionGateway.class);
		ConnectionDomainService service = new ConnectionDomainService(gateway);
		ConnectionE connectionE = newValidConnection();
		connectionE.setName("");

		Assertions.assertThatThrownBy(() -> service.createConnection(connectionE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createConnection(Mockito.any());
	}

	@Test
	@DisplayName("create connection with missing type fails")
	void createConnection_missingType_fail() {
		ConnectionGateway gateway = Mockito.mock(ConnectionGateway.class);
		ConnectionDomainService service = new ConnectionDomainService(gateway);
		ConnectionE connectionE = newValidConnection();
		connectionE.setType(null);

		Assertions.assertThatThrownBy(() -> service.createConnection(connectionE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createConnection(Mockito.any());
	}

	@Test
	@DisplayName("create connection with invalid type fails")
	void createConnection_invalidType_fail() {
		ConnectionGateway gateway = Mockito.mock(ConnectionGateway.class);
		ConnectionDomainService service = new ConnectionDomainService(gateway);
		ConnectionE connectionE = newValidConnection();
		connectionE.setType(6);

		Assertions.assertThatThrownBy(() -> service.createConnection(connectionE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createConnection(Mockito.any());
	}

	@Test
	@DisplayName("create connection with invalid JSON config fails")
	void createConnection_invalidConfig_fail() {
		ConnectionGateway gateway = Mockito.mock(ConnectionGateway.class);
		ConnectionDomainService service = new ConnectionDomainService(gateway);
		ConnectionE connectionE = newValidConnection();
		connectionE.setConfig("{invalid");

		Assertions.assertThatThrownBy(() -> service.createConnection(connectionE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createConnection(Mockito.any());
	}

	@Test
	@DisplayName("update connection that does not exist fails")
	void updateConnection_notFound_fail() {
		ConnectionGateway gateway = Mockito.mock(ConnectionGateway.class);
		Mockito.when(gateway.existsConnection(2L)).thenReturn(false);
		ConnectionDomainService service = new ConnectionDomainService(gateway);
		ConnectionE connectionE = newValidConnection();
		connectionE.setId(2L);

		Assertions.assertThatThrownBy(() -> service.updateConnection(connectionE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).updateConnection(Mockito.any());
	}

	@Test
	@DisplayName("delete connection with empty ids fails")
	void deleteConnection_emptyIds_fail() {
		ConnectionGateway gateway = Mockito.mock(ConnectionGateway.class);
		ConnectionDomainService service = new ConnectionDomainService(gateway);

		Assertions.assertThatThrownBy(() -> service.deleteConnection(new Long[] {})).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).deleteConnection(Mockito.any());
	}

}
