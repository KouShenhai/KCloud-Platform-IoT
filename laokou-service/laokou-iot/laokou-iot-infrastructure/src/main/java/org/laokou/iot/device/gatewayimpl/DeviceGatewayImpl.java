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

package org.laokou.iot.device.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.device.model.DeviceE;
import org.springframework.stereotype.Component;
import org.laokou.iot.device.gateway.DeviceGateway;
import org.laokou.iot.device.gatewayimpl.database.DeviceMapper;
import java.util.Arrays;
import org.laokou.iot.device.convertor.DeviceConvertor;
import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;

/**
 *
 * 设备网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeviceGatewayImpl implements DeviceGateway {

	private final DeviceMapper deviceMapper;

	@Override
	public void createDevice(DeviceE deviceE) {
		deviceMapper
			.insert(DeviceConvertor.toDataObject(1L, deviceE, true));
	}

	@Override
	public void updateDevice(DeviceE deviceE) {
		DeviceDO deviceDO = DeviceConvertor.toDataObject(null, deviceE, false);
		deviceDO.setVersion(deviceMapper.selectVersion(deviceE.getId()));
		deviceMapper.updateById(deviceDO);
	}

	@Override
	public void deleteDevice(Long[] ids) {
		deviceMapper.deleteByIds(Arrays.asList(ids));
	}

}
