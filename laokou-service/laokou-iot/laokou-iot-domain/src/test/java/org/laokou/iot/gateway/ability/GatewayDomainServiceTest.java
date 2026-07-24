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
import org.laokou.iot.gateway.gateway.GatewayGateway;
import org.laokou.iot.gateway.model.GatewayE;
import org.mockito.Mockito;

/**
 * 网关领域服务测试.
 *
 * @author laokou
 */
class GatewayDomainServiceTest {

	private GatewayE newValidGateway() {
		GatewayE gatewayE = new GatewayE();
		gatewayE.setGatewayKey("GW-0001");
		gatewayE.setName("测试网关");
		gatewayE.setStatus(0);
		gatewayE.setProductId(1L);
		return gatewayE;
	}

	@Test
	@DisplayName("新增网关-成功")
	void createGateway_success() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsGatewayKey(null, "GW-0001")).thenReturn(false);
		GatewayDomainService service = new GatewayDomainService(gateway);

		Assertions.assertThatCode(() -> service.createGateway(newValidGateway())).doesNotThrowAnyException();
		Mockito.verify(gateway).createGateway(Mockito.any(GatewayE.class));
	}

	@Test
	@DisplayName("新增网关-标识为空-失败")
	void createGateway_blankGatewayKey_fail() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		GatewayDomainService service = new GatewayDomainService(gateway);
		GatewayE gatewayE = newValidGateway();
		gatewayE.setGatewayKey("");

		Assertions.assertThatThrownBy(() -> service.createGateway(gatewayE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createGateway(Mockito.any());
	}

	@Test
	@DisplayName("新增网关-标识重复-失败")
	void createGateway_duplicateGatewayKey_fail() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsGatewayKey(null, "GW-0001")).thenReturn(true);
		GatewayDomainService service = new GatewayDomainService(gateway);

		Assertions.assertThatThrownBy(() -> service.createGateway(newValidGateway()))
			.isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createGateway(Mockito.any());
	}

	@Test
	@DisplayName("新增网关-产品不存在-失败")
	void createGateway_productNotFound_fail() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(false);
		GatewayDomainService service = new GatewayDomainService(gateway);

		Assertions.assertThatThrownBy(() -> service.createGateway(newValidGateway()))
			.isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createGateway(Mockito.any());
	}

	@Test
	@DisplayName("新增网关-状态非法-失败")
	void createGateway_invalidStatus_fail() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsGatewayKey(null, "GW-0001")).thenReturn(false);
		GatewayDomainService service = new GatewayDomainService(gateway);
		GatewayE gatewayE = newValidGateway();
		gatewayE.setStatus(9);

		Assertions.assertThatThrownBy(() -> service.createGateway(gatewayE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createGateway(Mockito.any());
	}

	@Test
	@DisplayName("修改网关-成功")
	void updateGateway_success() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsGateway(2L)).thenReturn(true);
		Mockito.when(gateway.existsGatewayKey(2L, "GW-0001")).thenReturn(false);
		GatewayDomainService service = new GatewayDomainService(gateway);
		GatewayE gatewayE = newValidGateway();
		gatewayE.setId(2L);

		Assertions.assertThatCode(() -> service.updateGateway(gatewayE)).doesNotThrowAnyException();
		Mockito.verify(gateway).updateGateway(Mockito.any(GatewayE.class));
	}

	@Test
	@DisplayName("修改网关-网关不存在-失败")
	void updateGateway_gatewayNotFound_fail() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Mockito.when(gateway.existsGateway(2L)).thenReturn(false);
		GatewayDomainService service = new GatewayDomainService(gateway);
		GatewayE gatewayE = newValidGateway();
		gatewayE.setId(2L);

		Assertions.assertThatThrownBy(() -> service.updateGateway(gatewayE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).updateGateway(Mockito.any());
	}

	@Test
	@DisplayName("删除网关-IDS为空-失败")
	void deleteGateway_emptyIds_fail() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		GatewayDomainService service = new GatewayDomainService(gateway);

		Assertions.assertThatThrownBy(() -> service.deleteGateway(new Long[] {})).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).deleteGateway(Mockito.any());
	}

	@Test
	@DisplayName("删除网关-成功")
	void deleteGateway_success() {
		GatewayGateway gateway = Mockito.mock(GatewayGateway.class);
		Long[] ids = { 1L, 2L };
		Mockito.when(gateway.existsGateway(ids)).thenReturn(true);
		GatewayDomainService service = new GatewayDomainService(gateway);

		Assertions.assertThatCode(() -> service.deleteGateway(ids)).doesNotThrowAnyException();
		Mockito.verify(gateway).deleteGateway(ids);
	}

}
