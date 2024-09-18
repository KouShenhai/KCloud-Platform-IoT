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

package org.laokou.iot.device.convertor;

import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.common.core.utils.ConvertUtil;
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
		DeviceDO deviceDO = ConvertUtil.sourceToTarget(deviceE, DeviceDO.class);
		if (isInsert) {
			deviceDO.generatorId();
		}
		return deviceDO;
	}

	public static DeviceCO toClientObject(DeviceDO deviceDO) {
		return ConvertUtil.sourceToTarget(deviceDO, DeviceCO.class);
	}

	public static DeviceE toEntity(DeviceCO deviceCO) {
		return ConvertUtil.sourceToTarget(deviceCO, DeviceE.class);
	}

}
