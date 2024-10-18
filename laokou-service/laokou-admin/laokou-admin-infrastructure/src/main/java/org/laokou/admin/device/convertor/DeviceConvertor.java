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

package org.laokou.admin.device.convertor;

import org.laokou.admin.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.admin.device.dto.clientobject.DeviceCO;
import org.laokou.admin.device.model.DeviceE;

/**
 *
 * 设备转换器.
 *
 * @author laokou
 */
public class DeviceConvertor {

	public static DeviceDO toDataObject(DeviceE deviceE, boolean isInsert) {
		DeviceDO deviceDO = new DeviceDO();
		if (isInsert) {
			deviceDO.generatorId();
		}
		else {
			deviceDO.setId(deviceE.getId());
		}
		deviceDO.setSn(deviceE.getSn());
		deviceDO.setName(deviceE.getName());
		deviceDO.setStatus(deviceE.getStatus());
		return deviceDO;
	}

	public static DeviceCO toClientObject(DeviceDO deviceDO) {
		DeviceCO deviceCO = new DeviceCO();
		deviceCO.setSn(deviceDO.getSn());
		deviceCO.setName(deviceDO.getName());
		deviceCO.setStatus(deviceDO.getStatus());
		return deviceCO;
	}

	public static DeviceE toEntity(DeviceCO deviceCO) {
		DeviceE deviceE = new DeviceE();
		deviceE.setSn(deviceCO.getSn());
		deviceE.setName(deviceCO.getName());
		deviceE.setStatus(deviceCO.getStatus());
		return deviceE;
	}

}
