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

package org.laokou.admin.device.model;

import lombok.Data;

/**
 *
 * 设备领域对象【实体】.
 *
 * @author laokou
 */
@Data
public class DeviceE {

	/**
	 * ID.
	 */
	private Long id;

	/**
	 * 设备序列号.
	 */
	private String sn;

	/**
	 * 设备名称.
	 */
	private String name;

	/**
	 * 设备状态 0在线 1离线.
	 */
	private Integer status;

}
