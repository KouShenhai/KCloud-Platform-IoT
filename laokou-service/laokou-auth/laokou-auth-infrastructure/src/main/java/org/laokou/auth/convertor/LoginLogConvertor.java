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

package org.laokou.auth.convertor;

import org.laokou.auth.dto.clientobject.LoginLogCO;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.auth.model.LoginLogE;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.JacksonUtil;

import static org.laokou.common.i18n.utils.StringUtil.truncate;

/**
 * @author laokou
 */
public final class LoginLogConvertor {

	private LoginLogConvertor() {
	}

	public static LoginLogE toEntity(LoginLogCO co) {
		LoginLogE loginLogE = DomainFactory.getLoginLog();
		loginLogE.setId(co.getId());
		loginLogE.setUsername(co.getUsername());
		loginLogE.setIp(co.getIp());
		loginLogE.setAddress(co.getAddress());
		loginLogE.setBrowser(co.getBrowser());
		loginLogE.setOs(co.getOs());
		loginLogE.setStatus(co.getStatus());
		loginLogE.setErrorMessage(co.getErrorMessage());
		loginLogE.setType(co.getType());
		loginLogE.setInstant(co.getInstant());
		loginLogE.setTenantId(co.getTenantId());
		return loginLogE;
	}

	public static LoginLogDO toDataObject(LoginLogE loginLogE) {
		LoginLogDO loginLogDO = new LoginLogDO();
		loginLogDO.setId(loginLogE.getId());
		loginLogDO.setUsername(loginLogE.getUsername());
		loginLogDO.setIp(loginLogE.getIp());
		loginLogDO.setAddress(loginLogE.getAddress());
		loginLogDO.setBrowser(loginLogE.getBrowser());
		loginLogDO.setOs(loginLogE.getOs());
		loginLogDO.setStatus(loginLogE.getStatus());
		loginLogDO.setErrorMessage(loginLogE.getErrorMessage());
		loginLogDO.setType(loginLogE.getType());
		loginLogDO.setCreateTime(loginLogE.getInstant());
		loginLogDO.setUpdateTime(loginLogE.getInstant());
		loginLogDO.setTenantId(loginLogE.getTenantId());
		return loginLogDO;
	}

	public static LoginLogCO toClientObject(DomainEvent domainEvent) {
		LoginEvent loginEvent = JacksonUtil.toBean(domainEvent.getPayload(), LoginEvent.class);
		LoginLogCO loginLogCO = new LoginLogCO();
		loginLogCO.setId(domainEvent.getAggregateId());
		loginLogCO.setUsername(loginEvent.username());
		loginLogCO.setIp(loginEvent.ip());
		loginLogCO.setAddress(loginEvent.address());
		loginLogCO.setBrowser(loginEvent.browser());
		loginLogCO.setOs(loginEvent.os());
		loginLogCO.setStatus(loginEvent.status());
		loginLogCO.setErrorMessage(truncate(loginEvent.errorMessage(), 2000));
		loginLogCO.setType(loginEvent.type());
		loginLogCO.setInstant(loginEvent.instant());
		loginLogCO.setTenantId(domainEvent.getTenantId());
		return loginLogCO;
	}

}
