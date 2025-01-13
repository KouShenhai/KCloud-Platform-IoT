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

package org.laokou.iot.device.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.device.gateway.*;
import org.laokou.iot.device.model.DeviceE;
import org.springframework.stereotype.Component;

/**
 *
 * 设备领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeviceDomainService {

	private final DeviceGateway deviceGateway;

	public void create(DeviceE deviceE) {
		deviceGateway.create(deviceE);
	}

	public void update(DeviceE deviceE) {
		deviceGateway.update(deviceE);
	}

	public void delete(Long[] ids) {
		deviceGateway.delete(ids);
	}

}
