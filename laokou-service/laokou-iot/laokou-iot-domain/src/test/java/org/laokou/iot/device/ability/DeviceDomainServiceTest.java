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

package org.laokou.iot.device.ability;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.iot.device.gateway.DeviceGateway;
import org.laokou.iot.device.model.DeviceE;
import org.mockito.Mockito;

/**
 * 设备领域服务测试.
 *
 * @author laokou
 */
class DeviceDomainServiceTest {

	private DeviceE newValidDevice() {
		DeviceE deviceE = new DeviceE();
		deviceE.setSn("SN-0001");
		deviceE.setName("测试设备");
		deviceE.setStatus(0);
		deviceE.setProductId(1L);
		return deviceE;
	}

	@Test
	@DisplayName("新增设备-成功")
	void createDevice_success() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsSn(null, "SN-0001")).thenReturn(false);
		DeviceDomainService service = new DeviceDomainService(gateway);

		Assertions.assertThatCode(() -> service.createDevice(newValidDevice())).doesNotThrowAnyException();
		Mockito.verify(gateway).createDevice(Mockito.any(DeviceE.class));
	}

	@Test
	@DisplayName("新增设备-序列号为空-失败")
	void createDevice_blankSn_fail() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		DeviceDomainService service = new DeviceDomainService(gateway);
		DeviceE deviceE = newValidDevice();
		deviceE.setSn("");

		Assertions.assertThatThrownBy(() -> service.createDevice(deviceE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createDevice(Mockito.any());
	}

	@Test
	@DisplayName("新增设备-名称为空-失败")
	void createDevice_blankName_fail() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsSn(null, "SN-0001")).thenReturn(false);
		DeviceDomainService service = new DeviceDomainService(gateway);
		DeviceE deviceE = newValidDevice();
		deviceE.setName(null);

		Assertions.assertThatThrownBy(() -> service.createDevice(deviceE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createDevice(Mockito.any());
	}

	@Test
	@DisplayName("新增设备-序列号重复-失败")
	void createDevice_duplicateSn_fail() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsSn(null, "SN-0001")).thenReturn(true);
		DeviceDomainService service = new DeviceDomainService(gateway);

		Assertions.assertThatThrownBy(() -> service.createDevice(newValidDevice())).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createDevice(Mockito.any());
	}

	@Test
	@DisplayName("新增设备-产品不存在-失败")
	void createDevice_productNotFound_fail() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(false);
		DeviceDomainService service = new DeviceDomainService(gateway);

		Assertions.assertThatThrownBy(() -> service.createDevice(newValidDevice())).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createDevice(Mockito.any());
	}

	@Test
	@DisplayName("新增设备-状态非法-失败")
	void createDevice_invalidStatus_fail() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsSn(null, "SN-0001")).thenReturn(false);
		DeviceDomainService service = new DeviceDomainService(gateway);
		DeviceE deviceE = newValidDevice();
		deviceE.setStatus(9);

		Assertions.assertThatThrownBy(() -> service.createDevice(deviceE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).createDevice(Mockito.any());
	}

	@Test
	@DisplayName("修改设备-成功")
	void updateDevice_success() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsProduct(1L)).thenReturn(true);
		Mockito.when(gateway.existsDevice(2L)).thenReturn(true);
		Mockito.when(gateway.existsSn(2L, "SN-0001")).thenReturn(false);
		DeviceDomainService service = new DeviceDomainService(gateway);
		DeviceE deviceE = newValidDevice();
		deviceE.setId(2L);

		Assertions.assertThatCode(() -> service.updateDevice(deviceE)).doesNotThrowAnyException();
		Mockito.verify(gateway).updateDevice(Mockito.any(DeviceE.class));
	}

	@Test
	@DisplayName("修改设备-设备不存在-失败")
	void updateDevice_deviceNotFound_fail() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Mockito.when(gateway.existsDevice(2L)).thenReturn(false);
		DeviceDomainService service = new DeviceDomainService(gateway);
		DeviceE deviceE = newValidDevice();
		deviceE.setId(2L);

		Assertions.assertThatThrownBy(() -> service.updateDevice(deviceE)).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).updateDevice(Mockito.any());
	}

	@Test
	@DisplayName("删除设备-IDS为空-失败")
	void deleteDevice_emptyIds_fail() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		DeviceDomainService service = new DeviceDomainService(gateway);

		Assertions.assertThatThrownBy(() -> service.deleteDevice(new Long[] {})).isInstanceOf(ParamException.class);
		Mockito.verify(gateway, Mockito.never()).deleteDevice(Mockito.any());
	}

	@Test
	@DisplayName("删除设备-成功")
	void deleteDevice_success() {
		DeviceGateway gateway = Mockito.mock(DeviceGateway.class);
		Long[] ids = { 1L, 2L };
		Mockito.when(gateway.existsDevice(ids)).thenReturn(true);
		DeviceDomainService service = new DeviceDomainService(gateway);

		Assertions.assertThatCode(() -> service.deleteDevice(ids)).doesNotThrowAnyException();
		Mockito.verify(gateway).deleteDevice(ids);
	}

}
