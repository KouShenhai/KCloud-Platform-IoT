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
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.iot.gateway.gateway.GatewayGateway;
import org.laokou.iot.gateway.model.GatewayE;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 *
 * 网关领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class GatewayDomainService {

	private final GatewayGateway gatewayGateway;

	public void createGateway(GatewayE gatewayE) {
		checkGatewayParam(gatewayE, false);
		gatewayGateway.createGateway(gatewayE);
	}

	public void updateGateway(GatewayE gatewayE) {
		checkGatewayParam(gatewayE, true);
		gatewayGateway.updateGateway(gatewayE);
	}

	public void deleteGateway(Long[] ids) {
		checkRemoveParam(ids);
		gatewayGateway.deleteGateway(ids);
	}

	private void checkGatewayParam(GatewayE gatewayE, boolean modify) {
		ParamValidator.validate("Gateway", validateCo(gatewayE), validateId(gatewayE, modify),
				validateGatewayKey(gatewayE), validateName(gatewayE), validateStatus(gatewayE),
				validateRemark(gatewayE), validateProduct(gatewayE), validateUniqueGatewayKey(gatewayE));
	}

	private void checkRemoveParam(Long[] ids) {
		ParamValidator.validate("Gateway", validateIds(ids));
	}

	private ParamValidator.Validate validateCo(GatewayE gatewayE) {
		if (ObjectUtils.isNull(gatewayE)) {
			return ParamValidator.invalidate("网关不能为空");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateId(GatewayE gatewayE, boolean modify) {
		if (ObjectUtils.isNull(gatewayE)) {
			return ParamValidator.validate();
		}
		if (modify && ObjectUtils.isNull(gatewayE.getId())) {
			return ParamValidator.invalidate("网关ID不能为空");
		}
		if (modify && gatewayE.getId() < 1) {
			return ParamValidator.invalidate("网关ID错误");
		}
		if (modify && !gatewayGateway.existsGateway(gatewayE.getId())) {
			return ParamValidator.invalidate("网关不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateGatewayKey(GatewayE gatewayE) {
		if (ObjectUtils.isNull(gatewayE)) {
			return ParamValidator.validate();
		}
		String gatewayKey = gatewayE.getGatewayKey();
		if (StringExtUtils.isEmpty(gatewayKey)) {
			return ParamValidator.invalidate("网关标识不能为空");
		}
		if (gatewayKey.length() > 64) {
			return ParamValidator.invalidate("网关标识不能超过64个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateName(GatewayE gatewayE) {
		if (ObjectUtils.isNull(gatewayE)) {
			return ParamValidator.validate();
		}
		String name = gatewayE.getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("网关名称不能为空");
		}
		if (name.length() > 50) {
			return ParamValidator.invalidate("网关名称不能超过50个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateStatus(GatewayE gatewayE) {
		if (ObjectUtils.isNull(gatewayE) || ObjectUtils.isNull(gatewayE.getStatus())) {
			return ParamValidator.invalidate("网关状态不能为空");
		}
		if (!List.of(0, 1).contains(gatewayE.getStatus())) {
			return ParamValidator.invalidate("网关状态不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateRemark(GatewayE gatewayE) {
		if (ObjectUtils.isNull(gatewayE) || StringExtUtils.isEmpty(gatewayE.getRemark())) {
			return ParamValidator.validate();
		}
		if (gatewayE.getRemark().length() > 400) {
			return ParamValidator.invalidate("网关备注不能超过400个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateProduct(GatewayE gatewayE) {
		if (ObjectUtils.isNull(gatewayE)) {
			return ParamValidator.validate();
		}
		if (ObjectUtils.isNull(gatewayE.getProductId())) {
			return ParamValidator.invalidate("产品ID不能为空");
		}
		if (!gatewayGateway.existsProduct(gatewayE.getProductId())) {
			return ParamValidator.invalidate("产品不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateUniqueGatewayKey(GatewayE gatewayE) {
		if (ObjectUtils.isNull(gatewayE) || StringExtUtils.isEmpty(gatewayE.getGatewayKey())) {
			return ParamValidator.validate();
		}
		if (gatewayGateway.existsGatewayKey(gatewayE.getId(), gatewayE.getGatewayKey())) {
			return ParamValidator.invalidate("网关标识已存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateIds(Long[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return ParamValidator.invalidate("网关IDS不能为空");
		}
		if (Arrays.stream(ids).anyMatch(id -> ObjectUtils.isNull(id) || id < 1)) {
			return ParamValidator.invalidate("网关IDS错误");
		}
		if (!gatewayGateway.existsGateway(ids)) {
			return ParamValidator.invalidate("网关不存在");
		}
		return ParamValidator.validate();
	}

}
