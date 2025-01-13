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

package org.laokou.iot.device.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.device.convertor.DeviceConvertor;
import org.laokou.iot.device.dto.DevicePageQry;
import org.laokou.iot.device.dto.clientobject.DeviceCO;
import org.laokou.iot.device.gatewayimpl.database.DeviceMapper;
import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页查询设备请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DevicePageQryExe {

	private final DeviceMapper deviceMapper;

	public Result<Page<DeviceCO>> execute(DevicePageQry qry) {
		List<DeviceDO> list = deviceMapper.selectObjectPage(qry);
		long total = deviceMapper.selectObjectCount(qry);
		return Result.ok(Page.create(list.stream().map(DeviceConvertor::toClientObject).toList(), total));
	}

}
