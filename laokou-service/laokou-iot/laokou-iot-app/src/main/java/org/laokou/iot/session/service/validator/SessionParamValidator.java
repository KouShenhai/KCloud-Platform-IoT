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

package org.laokou.iot.session.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.session.gatewayimpl.database.SessionMapper;
import org.laokou.iot.session.gatewayimpl.database.dataobject.SessionDO;
import org.laokou.iot.session.model.SessionA;
import org.laokou.iot.session.model.entity.SessionE;
import org.springframework.util.StringUtils;

/**
 * @author laokou
 */
final class SessionParamValidator {

	private SessionParamValidator() {

	}

	static ParamValidator.Validate validateId(SessionA sessionA) {
		Long id = sessionA.getId();
		if (id == null) {
			return ParamValidator.invalidate("会话ID不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateName(SessionA sessionA) {
		if (!StringUtils.hasText(sessionA.getSessionE().getName())) {
			return ParamValidator.invalidate("会话名称不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateUsername(SessionA sessionA) {
		if (!StringUtils.hasText(sessionA.getSessionE().getUsername())) {
			return ParamValidator.invalidate("会话用户名不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validatePassword(SessionA sessionA) {
		if (!StringUtils.hasText(sessionA.getSessionE().getPassword())) {
			return ParamValidator.invalidate("会话密码不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateHostAndPort(SessionA sessionA, SessionMapper sessionMapper) {
		SessionE sessionE = sessionA.getSessionE();
		Long id = sessionE.getId();
		String host = sessionE.getHost();
		Integer port = sessionE.getPort();
		if (!StringUtils.hasText(host) || ObjectUtils.isNull(port)) {
			return ParamValidator.invalidate("会话主机和会话端口不能为空");
		}
		if (!RegexUtils.ipv4Regex(host)) {
			return ParamValidator.invalidate("会话主机IP地址格式错误");
		}
		if (sessionA.isSave() && sessionMapper.selectCount(Wrappers.lambdaQuery(SessionDO.class)
			.eq(SessionDO::getHost, host)
			.eq(SessionDO::getPort, port)
			.eq(SessionDO::getTenantId, UserUtils.getTenantId())) > 0) {
			return ParamValidator.invalidate("会话主机和会话端口已存在");
		}
		if (sessionA.isModify() && sessionMapper.selectCount(Wrappers.lambdaQuery(SessionDO.class)
			.eq(SessionDO::getHost, host)
			.eq(SessionDO::getPort, port)
			.eq(SessionDO::getTenantId, UserUtils.getTenantId())
			.ne(SessionDO::getId, id)) > 0) {
			return ParamValidator.invalidate("会话主机和会话端口已存在");
		}
		return ParamValidator.validate();
	}

}
