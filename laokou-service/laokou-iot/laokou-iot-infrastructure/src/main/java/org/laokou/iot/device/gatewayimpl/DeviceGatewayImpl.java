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
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.iot.device.convertor.DeviceConvertor;
import org.laokou.iot.device.gateway.DeviceGateway;
import org.laokou.iot.device.gatewayimpl.database.DeviceMapper;
import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.iot.device.model.DeviceE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 *
 * 设备网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceGatewayImpl implements DeviceGateway {

	private final DeviceMapper deviceMapper;

	private final TransactionalUtil transactionalUtil;

	public void create(DeviceE deviceE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deviceMapper.insert(DeviceConvertor.toDataObject(deviceE, true));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(DeviceE deviceE) {
		DeviceDO deviceDO = DeviceConvertor.toDataObject(deviceE, false);
		deviceDO.setVersion(deviceMapper.selectVersion(deviceE.getId()));
		update(deviceDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deviceMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(DeviceDO deviceDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deviceMapper.updateById(deviceDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("修改失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

}
