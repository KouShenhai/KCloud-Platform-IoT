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

package org.laokou.iot.gateway.ability;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.iot.gateway.gateway.GatewayCommandGateway;
import org.laokou.iot.gateway.gateway.GatewayGateway;
import org.laokou.iot.gateway.model.GatewayCommandE;
import org.mockito.Mockito;

/**
 * 网关指令领域服务测试.
 *
 * @author laokou
 */
class GatewayCommandDomainServiceTest {

	private GatewayCommandE newRebootCommand() {
		GatewayCommandE gatewayCommandE = new GatewayCommandE();
		gatewayCommandE.setCommandId(100L);
		gatewayCommandE.setGatewayId(1L);
		gatewayCommandE.setGatewayKey("GW-0001");
		gatewayCommandE.setType(GatewayCommandE.TYPE_REBOOT);
		gatewayCommandE.setStatus(GatewayCommandE.STATUS_PENDING);
		gatewayCommandE.setPayload("{}");
		return gatewayCommandE;
	}

	@Test
	@DisplayName("下发重启指令-成功")
	void dispatchReboot_success() {
		GatewayGateway gatewayGateway = Mockito.mock(GatewayGateway.class);
		GatewayCommandGateway gatewayCommandGateway = Mockito.mock(GatewayCommandGateway.class);
		Mockito.when(gatewayGateway.existsGateway(1L)).thenReturn(true);
		GatewayCommandDomainService service = new GatewayCommandDomainService(gatewayGateway, gatewayCommandGateway);

		Assertions.assertThatCode(() -> service.dispatch(newRebootCommand())).doesNotThrowAnyException();
		Mockito.verify(gatewayCommandGateway).saveLog(Mockito.any(GatewayCommandE.class));
		Mockito.verify(gatewayCommandGateway).publish(Mockito.any(GatewayCommandE.class));
	}

	@Test
	@DisplayName("下发指令-网关不存在-失败")
	void dispatch_gatewayNotFound_fail() {
		GatewayGateway gatewayGateway = Mockito.mock(GatewayGateway.class);
		GatewayCommandGateway gatewayCommandGateway = Mockito.mock(GatewayCommandGateway.class);
		Mockito.when(gatewayGateway.existsGateway(1L)).thenReturn(false);
		GatewayCommandDomainService service = new GatewayCommandDomainService(gatewayGateway, gatewayCommandGateway);

		Assertions.assertThatThrownBy(() -> service.dispatch(newRebootCommand())).isInstanceOf(ParamException.class);
		Mockito.verify(gatewayCommandGateway, Mockito.never()).saveLog(Mockito.any());
		Mockito.verify(gatewayCommandGateway, Mockito.never()).publish(Mockito.any());
	}

	@Test
	@DisplayName("下发读取属性指令-设备标识为空-失败")
	void dispatchReadProperty_blankDeviceKey_fail() {
		GatewayGateway gatewayGateway = Mockito.mock(GatewayGateway.class);
		GatewayCommandGateway gatewayCommandGateway = Mockito.mock(GatewayCommandGateway.class);
		Mockito.when(gatewayGateway.existsGateway(1L)).thenReturn(true);
		GatewayCommandDomainService service = new GatewayCommandDomainService(gatewayGateway, gatewayCommandGateway);
		GatewayCommandE gatewayCommandE = newRebootCommand();
		gatewayCommandE.setType(GatewayCommandE.TYPE_READ_PROPERTY);
		gatewayCommandE.setDeviceKey("");
		gatewayCommandE.setPayload("[\"temp\"]");

		Assertions.assertThatThrownBy(() -> service.dispatch(gatewayCommandE)).isInstanceOf(ParamException.class);
		Mockito.verify(gatewayCommandGateway, Mockito.never()).publish(Mockito.any());
	}

	@Test
	@DisplayName("下发写入属性指令-属性为空-失败")
	void dispatchWriteProperty_blankPayload_fail() {
		GatewayGateway gatewayGateway = Mockito.mock(GatewayGateway.class);
		GatewayCommandGateway gatewayCommandGateway = Mockito.mock(GatewayCommandGateway.class);
		Mockito.when(gatewayGateway.existsGateway(1L)).thenReturn(true);
		GatewayCommandDomainService service = new GatewayCommandDomainService(gatewayGateway, gatewayCommandGateway);
		GatewayCommandE gatewayCommandE = newRebootCommand();
		gatewayCommandE.setType(GatewayCommandE.TYPE_WRITE_PROPERTY);
		gatewayCommandE.setDeviceKey("device-001");
		gatewayCommandE.setPayload(null);

		Assertions.assertThatThrownBy(() -> service.dispatch(gatewayCommandE)).isInstanceOf(ParamException.class);
		Mockito.verify(gatewayCommandGateway, Mockito.never()).publish(Mockito.any());
	}

	@Test
	@DisplayName("处理指令回执-更新状态")
	void handleReply_updatesStatus() {
		GatewayGateway gatewayGateway = Mockito.mock(GatewayGateway.class);
		GatewayCommandGateway gatewayCommandGateway = Mockito.mock(GatewayCommandGateway.class);
		GatewayCommandDomainService service = new GatewayCommandDomainService(gatewayGateway, gatewayCommandGateway);

		service.handleReply(100L, 1, "ok");
		Mockito.verify(gatewayCommandGateway).updateLogStatus(100L, 1, "ok");
	}

}
