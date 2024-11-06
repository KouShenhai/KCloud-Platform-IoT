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

package org.laokou.iot.device.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.device.model.DeviceE;
import org.springframework.stereotype.Component;
import org.laokou.iot.device.gateway.DeviceGateway;
import org.laokou.iot.device.gatewayimpl.database.DeviceMapper;
import java.util.Arrays;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
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

	private final TransactionalUtil transactionalUtil;

	@Override
	public void create(DeviceE deviceE) {
		transactionalUtil.executeInTransaction(() -> deviceMapper.insert(DeviceConvertor.toDataObject(deviceE, true)));
	}

	@Override
	public void update(DeviceE deviceE) {
		DeviceDO deviceDO = DeviceConvertor.toDataObject(deviceE, false);
		deviceDO.setVersion(deviceMapper.selectVersion(deviceE.getId()));
		update(deviceDO);
	}

	@Override
	public void delete(Long[] ids) {
		transactionalUtil.executeInTransaction(() -> deviceMapper.deleteByIds(Arrays.asList(ids)));
	}

	private void update(DeviceDO deviceDO) {
		transactionalUtil.executeInTransaction(() -> deviceMapper.updateById(deviceDO));
	}

}
