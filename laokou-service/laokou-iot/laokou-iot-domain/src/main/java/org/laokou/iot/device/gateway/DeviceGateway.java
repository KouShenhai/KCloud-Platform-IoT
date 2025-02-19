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

package org.laokou.iot.device.gateway;

import org.laokou.iot.device.model.DeviceE;

/**
 *
 * 设备网关【防腐】.
 *
 * @author laokou
 */
public interface DeviceGateway {

	/**
	 * 新增设备.
	 */
	void create(DeviceE deviceE);

	/**
	 * 修改设备.
	 */
	void update(DeviceE deviceE);

	/**
	 * 删除设备.
	 */
	void delete(Long[] ids);

}
