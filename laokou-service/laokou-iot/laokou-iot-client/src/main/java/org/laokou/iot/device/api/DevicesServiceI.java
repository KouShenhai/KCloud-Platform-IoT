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

package org.laokou.iot.device.api;

import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.device.dto.*;
import org.laokou.iot.device.dto.clientobject.DeviceCO;

/**
 *
 * 设备接口.
 *
 * @author laokou
 */
public interface DevicesServiceI {

	/**
	 * 保存设备.
	 * @param cmd 保存命令
	 */
	void save(DeviceSaveCmd cmd);

	/**
	 * 修改设备.
	 * @param cmd 修改命令
	 */
	void modify(DeviceModifyCmd cmd);

	/**
	 * 删除设备.
	 * @param cmd 删除命令
	 */
	void remove(DeviceRemoveCmd cmd);

	/**
	 * 导入设备.
	 * @param cmd 导入命令
	 */
	void importI(DeviceImportCmd cmd);

	/**
	 * 导出设备.
	 * @param cmd 导出命令
	 */
	void export(DeviceExportCmd cmd);

	/**
	 * 分页查询设备.
	 * @param qry 分页查询请求
	 */
	Result<Page<DeviceCO>> page(DevicePageQry qry);

	/**
	 * 查看设备.
	 * @param qry 查看请求
	 */
	Result<DeviceCO> getById(DeviceGetQry qry);

}
