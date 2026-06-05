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

package org.laokou.iot.device.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.iot.device.gateway.DeviceGateway;
import org.laokou.iot.device.model.DeviceE;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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

	public void createDevice(DeviceE deviceE) {
		checkDeviceParam(deviceE, false);
		deviceGateway.createDevice(deviceE);
	}

	public void updateDevice(DeviceE deviceE) {
		checkDeviceParam(deviceE, true);
		deviceGateway.updateDevice(deviceE);
	}

	public void deleteDevice(Long[] ids) {
		checkRemoveParam(ids);
		deviceGateway.deleteDevice(ids);
	}

	private void checkDeviceParam(DeviceE deviceE, boolean modify) {
		ParamValidator.validate("Device", validateCo(deviceE), validateId(deviceE, modify), validateSn(deviceE),
				validateName(deviceE), validateStatus(deviceE), validateRemark(deviceE), validateProduct(deviceE),
				validateUniqueSn(deviceE));
	}

	private void checkRemoveParam(Long[] ids) {
		ParamValidator.validate("Device", validateIds(ids));
	}

	private ParamValidator.Validate validateCo(DeviceE deviceE) {
		if (ObjectUtils.isNull(deviceE)) {
			return ParamValidator.invalidate("设备不能为空");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateId(DeviceE deviceE, boolean modify) {
		if (ObjectUtils.isNull(deviceE)) {
			return ParamValidator.validate();
		}
		if (modify && ObjectUtils.isNull(deviceE.getId())) {
			return ParamValidator.invalidate("设备ID不能为空");
		}
		if (modify && deviceE.getId() < 1) {
			return ParamValidator.invalidate("设备ID错误");
		}
		if (modify && !deviceGateway.existsDevice(deviceE.getId())) {
			return ParamValidator.invalidate("设备不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateSn(DeviceE deviceE) {
		if (ObjectUtils.isNull(deviceE)) {
			return ParamValidator.validate();
		}
		String sn = deviceE.getSn();
		if (StringExtUtils.isEmpty(sn)) {
			return ParamValidator.invalidate("设备序列号不能为空");
		}
		if (sn.length() > 64) {
			return ParamValidator.invalidate("设备序列号不能超过64个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateName(DeviceE deviceE) {
		if (ObjectUtils.isNull(deviceE)) {
			return ParamValidator.validate();
		}
		String name = deviceE.getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("设备名称不能为空");
		}
		if (name.length() > 50) {
			return ParamValidator.invalidate("设备名称不能超过50个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateStatus(DeviceE deviceE) {
		if (ObjectUtils.isNull(deviceE) || ObjectUtils.isNull(deviceE.getStatus())) {
			return ParamValidator.invalidate("设备状态不能为空");
		}
		if (!List.of(0, 1).contains(deviceE.getStatus())) {
			return ParamValidator.invalidate("设备状态不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateRemark(DeviceE deviceE) {
		if (ObjectUtils.isNull(deviceE) || StringExtUtils.isEmpty(deviceE.getRemark())) {
			return ParamValidator.validate();
		}
		if (deviceE.getRemark().length() > 400) {
			return ParamValidator.invalidate("设备备注不能超过400个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateProduct(DeviceE deviceE) {
		if (ObjectUtils.isNull(deviceE)) {
			return ParamValidator.validate();
		}
		if (ObjectUtils.isNull(deviceE.getProductId())) {
			return ParamValidator.invalidate("产品ID不能为空");
		}
		if (!deviceGateway.existsProduct(deviceE.getProductId())) {
			return ParamValidator.invalidate("产品不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateUniqueSn(DeviceE deviceE) {
		if (ObjectUtils.isNull(deviceE) || StringExtUtils.isEmpty(deviceE.getSn())) {
			return ParamValidator.validate();
		}
		if (deviceGateway.existsSn(deviceE.getId(), deviceE.getSn())) {
			return ParamValidator.invalidate("设备序列号已存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateIds(Long[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return ParamValidator.invalidate("设备IDS不能为空");
		}
		if (Arrays.stream(ids).anyMatch(id -> ObjectUtils.isNull(id) || id < 1)) {
			return ParamValidator.invalidate("设备IDS错误");
		}
		if (!deviceGateway.existsDevice(ids)) {
			return ParamValidator.invalidate("设备不存在");
		}
		return ParamValidator.validate();
	}

}
