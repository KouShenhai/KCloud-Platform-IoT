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

package org.laokou.iot.session.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.network.connection.gateway.ConnectionGateway;
import org.laokou.network.connection.model.ConnectionE;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Network connection domain service.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ConnectionDomainService {

	private static final List<Integer> CONNECTION_TYPES = List.of(1, 2, 3, 4, 5);

	private static final List<Integer> ENABLED_VALUES = List.of(0, 1);

	private final ConnectionGateway connectionGateway;

	public void createConnection(ConnectionE connectionE) {
		checkConnectionParam(connectionE, false);
		connectionGateway.createConnection(connectionE);
	}

	public void updateConnection(ConnectionE connectionE) {
		checkConnectionParam(connectionE, true);
		connectionGateway.updateConnection(connectionE);
	}

	public void deleteConnection(Long[] ids) {
		checkRemoveParam(ids);
		connectionGateway.deleteConnection(ids);
	}

	private void checkConnectionParam(ConnectionE connectionE, boolean modify) {
		ParamValidator.validate("Connection", validateCo(connectionE), validateId(connectionE, modify),
				validateName(connectionE), validateType(connectionE), validateTypeImmutable(connectionE, modify),
				validateEnabled(connectionE), validateHost(connectionE), validatePort(connectionE),
				validateConfig(connectionE), validateRemark(connectionE));
	}

	private void checkRemoveParam(Long[] ids) {
		ParamValidator.validate("Connection", validateIds(ids));
	}

	private ParamValidator.Validate validateCo(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE)) {
			return ParamValidator.invalidate("Connection cannot be null");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateId(ConnectionE connectionE, boolean modify) {
		if (ObjectUtils.isNull(connectionE)) {
			return ParamValidator.validate();
		}
		if (modify && ObjectUtils.isNull(connectionE.getId())) {
			return ParamValidator.invalidate("Connection ID cannot be null");
		}
		if (modify && connectionE.getId() < 1) {
			return ParamValidator.invalidate("Connection ID is invalid");
		}
		if (modify && !connectionGateway.existsConnection(connectionE.getId())) {
			return ParamValidator.invalidate("Connection does not exist");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateName(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE)) {
			return ParamValidator.validate();
		}
		String name = connectionE.getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("Connection name cannot be empty");
		}
		if (name.length() > 100) {
			return ParamValidator.invalidate("Connection name cannot exceed 100 characters");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateType(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE)) {
			return ParamValidator.validate();
		}
		Integer type = connectionE.getType();
		if (ObjectUtils.isNull(type)) {
			return ParamValidator.invalidate("Connection type cannot be null");
		}
		if (!CONNECTION_TYPES.contains(type)) {
			return ParamValidator.invalidate("Connection type does not exist");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateTypeImmutable(ConnectionE connectionE, boolean modify) {
		if (!modify || ObjectUtils.isNull(connectionE) || ObjectUtils.isNull(connectionE.getId())
				|| ObjectUtils.isNull(connectionE.getType())) {
			return ParamValidator.validate();
		}
		Integer currentType = connectionGateway.getConnectionType(connectionE.getId());
		if (ObjectUtils.isNotNull(currentType) && !currentType.equals(connectionE.getType())) {
			return ParamValidator.invalidate("Connection type cannot be changed");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateEnabled(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE)) {
			return ParamValidator.validate();
		}
		Integer enabled = connectionE.getEnabled();
		if (ObjectUtils.isNull(enabled)) {
			return ParamValidator.invalidate("Connection enabled status cannot be null");
		}
		if (!ENABLED_VALUES.contains(enabled)) {
			return ParamValidator.invalidate("Connection enabled status does not exist");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateHost(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE) || StringExtUtils.isEmpty(connectionE.getHost())) {
			return ParamValidator.validate();
		}
		if (connectionE.getHost().length() > 255) {
			return ParamValidator.invalidate("Connection host cannot exceed 255 characters");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validatePort(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE) || ObjectUtils.isNull(connectionE.getPort())) {
			return ParamValidator.validate();
		}
		int port = connectionE.getPort();
		if (port < 1 || port > 65535) {
			return ParamValidator.invalidate("Connection port is invalid");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateConfig(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE)) {
			return ParamValidator.validate();
		}
		String config = connectionE.getConfig();
		if (StringExtUtils.isEmpty(config)) {
			return ParamValidator.invalidate("Connection config cannot be empty");
		}
		try {
			JacksonUtils.readTree(config);
		}
		catch (Exception ex) {
			return ParamValidator.invalidate("Connection config must be valid JSON");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateRemark(ConnectionE connectionE) {
		if (ObjectUtils.isNull(connectionE) || StringExtUtils.isEmpty(connectionE.getRemark())) {
			return ParamValidator.validate();
		}
		if (connectionE.getRemark().length() > 400) {
			return ParamValidator.invalidate("Connection remark cannot exceed 400 characters");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateIds(Long[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return ParamValidator.invalidate("Connection IDs cannot be empty");
		}
		if (Arrays.stream(ids).anyMatch(id -> ObjectUtils.isNull(id) || id < 1)) {
			return ParamValidator.invalidate("Connection IDs are invalid");
		}
		if (!connectionGateway.existsConnection(ids)) {
			return ParamValidator.invalidate("Connection does not exist");
		}
		return ParamValidator.validate();
	}

}
