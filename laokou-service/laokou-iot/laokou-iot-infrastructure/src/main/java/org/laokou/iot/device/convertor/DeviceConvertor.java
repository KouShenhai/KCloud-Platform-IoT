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

package org.laokou.iot.device.convertor;

import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.iot.device.dto.clientobject.DeviceCO;
import org.laokou.iot.device.model.DeviceE;

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
		deviceDO.setLongitude(deviceE.getLongitude());
		deviceDO.setLatitude(deviceE.getLatitude());
		deviceDO.setImgUrl(deviceE.getImgUrl());
		deviceDO.setAddress(deviceE.getAddress());
		deviceDO.setRemark(deviceE.getRemark());
		deviceDO.setProductId(deviceE.getProductId());
		return deviceDO;
	}

	public static DeviceCO toClientObject(DeviceDO deviceDO) {
		DeviceCO deviceCO = new DeviceCO();
		deviceCO.setSn(deviceDO.getSn());
		deviceCO.setName(deviceDO.getName());
		deviceCO.setStatus(deviceDO.getStatus());
		deviceCO.setLongitude(deviceDO.getLongitude());
		deviceCO.setLatitude(deviceDO.getLatitude());
		deviceCO.setImgUrl(deviceDO.getImgUrl());
		deviceCO.setAddress(deviceDO.getAddress());
		deviceCO.setRemark(deviceDO.getRemark());
		deviceCO.setProductId(deviceDO.getProductId());
		return deviceCO;
	}

	public static DeviceE toEntity(DeviceCO deviceCO) {
		DeviceE deviceE = new DeviceE();
		deviceE.setSn(deviceCO.getSn());
		deviceE.setName(deviceCO.getName());
		deviceE.setStatus(deviceCO.getStatus());
		deviceE.setLongitude(deviceCO.getLongitude());
		deviceE.setLatitude(deviceCO.getLatitude());
		deviceE.setImgUrl(deviceCO.getImgUrl());
		deviceE.setAddress(deviceCO.getAddress());
		deviceE.setRemark(deviceCO.getRemark());
		deviceE.setProductId(deviceCO.getProductId());
		return deviceE;
	}

}
