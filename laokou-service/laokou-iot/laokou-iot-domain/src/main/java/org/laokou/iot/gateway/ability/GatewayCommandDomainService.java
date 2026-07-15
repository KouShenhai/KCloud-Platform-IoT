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

package org.laokou.iot.gateway.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.iot.gateway.gateway.GatewayCommandGateway;
import org.laokou.iot.gateway.gateway.GatewayGateway;
import org.laokou.iot.gateway.model.GatewayCommandE;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 网关指令领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GatewayCommandDomainService {

	private final GatewayGateway gatewayGateway;

	private final GatewayCommandGateway gatewayCommandGateway;

	public void dispatch(GatewayCommandE gatewayCommandE) {
		checkCommandParam(gatewayCommandE);
		gatewayCommandGateway.saveLog(gatewayCommandE);
		gatewayCommandGateway.publish(gatewayCommandE);
	}

	public void handleReply(Long commandId, Integer status, String result) {
		gatewayCommandGateway.updateLogStatus(commandId, status, result);
	}

	private void checkCommandParam(GatewayCommandE gatewayCommandE) {
		ParamValidator.validate("GatewayCommand", validateCommand(gatewayCommandE), validateGateway(gatewayCommandE),
				validatePayload(gatewayCommandE));
	}

	private ParamValidator.Validate validateCommand(GatewayCommandE gatewayCommandE) {
		if (ObjectUtils.isNull(gatewayCommandE)) {
			return ParamValidator.invalidate("网关指令不能为空");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateGateway(GatewayCommandE gatewayCommandE) {
		if (ObjectUtils.isNull(gatewayCommandE)) {
			return ParamValidator.validate();
		}
		Long gatewayId = gatewayCommandE.getGatewayId();
		if (ObjectUtils.isNull(gatewayId) || gatewayId < 1) {
			return ParamValidator.invalidate("网关ID错误");
		}
		if (!gatewayGateway.existsGateway(gatewayId)) {
			return ParamValidator.invalidate("网关不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validatePayload(GatewayCommandE gatewayCommandE) {
		if (ObjectUtils.isNull(gatewayCommandE)) {
			return ParamValidator.validate();
		}
		Integer type = gatewayCommandE.getType();
		if (ObjectUtils.isNull(type) || !List
			.of(GatewayCommandE.TYPE_REBOOT, GatewayCommandE.TYPE_READ_PROPERTY, GatewayCommandE.TYPE_WRITE_PROPERTY)
			.contains(type)) {
			return ParamValidator.invalidate("网关指令类型不存在");
		}
		if (type == GatewayCommandE.TYPE_REBOOT) {
			return ParamValidator.validate();
		}
		// 读取/写入设备属性需要设备标识
		if (StringExtUtils.isEmpty(gatewayCommandE.getDeviceKey())) {
			return ParamValidator.invalidate("设备标识不能为空");
		}
		// 读取/写入设备属性需要指令内容
		if (StringExtUtils.isEmpty(gatewayCommandE.getPayload())) {
			return ParamValidator.invalidate("设备属性不能为空");
		}
		return ParamValidator.validate();
	}

}
