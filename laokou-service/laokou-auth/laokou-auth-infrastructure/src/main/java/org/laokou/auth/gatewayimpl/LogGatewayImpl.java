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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.gateway.LogGateway;
import org.laokou.auth.gatewayimpl.database.LoginLogMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LogGatewayImpl implements LogGateway {

	private final LoginLogMapper loginLogMapper;

	// @Override
	// public void create(LoginEvent event, DefaultDomainEvent evt) {
	// try {
	// DynamicDataSourceContextHolder.push(evt.getSourceName());
	// DynamicTableSuffixContextHolder.set(UNDER.concat(DateUtil.format(DateUtil.now(),
	// DateUtil.YYYYMM)));
	// loginLogMapper.insert(convert(event, evt));
	// }
	// finally {
	// DynamicTableSuffixContextHolder.clear();
	// DynamicDataSourceContextHolder.clear();
	// }
	// }

	private LoginLogDO convert(LoginEvent loginEvent, DefaultDomainEvent evt) {
		LoginLogDO logDO = new LoginLogDO();
		logDO.setUsername(loginEvent.getUsername());
		logDO.setIp(loginEvent.getIp());
		logDO.setAddress(loginEvent.getAddress());
		logDO.setBrowser(loginEvent.getBrowser());
		logDO.setOs(loginEvent.getOs());
		logDO.setStatus(loginEvent.getStatus());
		logDO.setErrorMessage(loginEvent.getErrorMessage());
		logDO.setType(loginEvent.getType());
		logDO.setId(IdGenerator.defaultSnowflakeId());
		logDO.setEditor(evt.getEditor());
		logDO.setCreator(evt.getCreator());
		logDO.setCreateDate(evt.getCreateDate());
		logDO.setUpdateDate(evt.getUpdateDate());
		logDO.setDeptId(evt.getDeptId());
		logDO.setDeptPath(evt.getDeptPath());
		logDO.setTenantId(evt.getTenantId());
		logDO.setEventId(evt.getId());
		return logDO;
	}

}
