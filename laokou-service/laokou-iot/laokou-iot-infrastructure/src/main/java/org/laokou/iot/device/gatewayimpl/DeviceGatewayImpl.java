/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.iot.device.model.DeviceE;
import org.springframework.stereotype.Component;
import org.laokou.iot.device.gateway.DeviceGateway;
import org.laokou.iot.device.gatewayimpl.database.DeviceMapper;
import java.util.Arrays;
import org.laokou.iot.device.convertor.DeviceConvertor;
import org.laokou.iot.device.gatewayimpl.database.dataobject.DeviceDO;
import org.laokou.iot.product.gatewayimpl.database.ProductMapper;
import org.laokou.iot.product.gatewayimpl.database.dataobject.ProductDO;

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

	private final ProductMapper productMapper;

	@Override
	public void createDevice(DeviceE deviceE) {
		deviceMapper.insert(DeviceConvertor.toDataObject(1L, deviceE, true));
	}

	@Override
	public void updateDevice(DeviceE deviceE) {
		DeviceDO deviceDO = DeviceConvertor.toDataObject(null, deviceE, false);
		deviceDO.setVersion(deviceMapper.selectVersion(deviceE.getId()));
		deviceMapper.updateById(deviceDO);
	}

	@Override
	public void deleteDevice(Long[] ids) {
		deviceMapper.deleteByIds(Arrays.asList(ids));
	}

	@Override
	public boolean existsSn(Long id, String sn) {
		return deviceMapper.selectCount(Wrappers.lambdaQuery(DeviceDO.class)
			.eq(DeviceDO::getSn, sn)
			.ne(ObjectUtils.isNotNull(id), DeviceDO::getId, id)) > 0;
	}

	@Override
	public boolean existsDevice(Long id) {
		return deviceMapper.selectCount(Wrappers.lambdaQuery(DeviceDO.class).eq(DeviceDO::getId, id)) > 0;
	}

	@Override
	public boolean existsDevice(Long[] ids) {
		return deviceMapper
			.selectCount(Wrappers.lambdaQuery(DeviceDO.class).in(DeviceDO::getId, Arrays.asList(ids))) == ids.length;
	}

	@Override
	public boolean existsProduct(Long productId) {
		return productMapper.selectCount(Wrappers.lambdaQuery(ProductDO.class).eq(ProductDO::getId, productId)) > 0;
	}

}
