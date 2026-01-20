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

package org.laokou.auth.convertor;

import com.blueconic.browscap.Capabilities;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.laokou.auth.dto.clientobject.LoginLogCO;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.entity.LoginLogE;
import org.laokou.auth.model.enums.LoginStatus;
import org.laokou.auth.model.entity.UserE;
import org.laokou.common.core.util.AddressUtils;
import org.laokou.common.core.util.IpUtils;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.ObjectUtils;

import java.util.Optional;

/**
 * @author laokou
 */
public final class LoginLogConvertor {

	private LoginLogConvertor() {
	}

	public static LoginLogE toEntity(LoginLogCO co) {
		return DomainFactory.getLoginLog()
			.toBuilder()
			.id(co.getId())
			.username(co.getUsername())
			.ip(co.getIp())
			.address(co.getAddress())
			.browser(co.getBrowser())
			.os(co.getOs())
			.status(co.getStatus())
			.errorMessage(co.getErrorMessage())
			.type(co.getType())
			.loginTime(co.getLoginTime())
			.tenantId(co.getTenantId())
			.creator(co.getCreator())
			.build();
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
		loginLogDO.setCreateTime(loginLogE.getLoginTime());
		loginLogDO.setUpdateTime(loginLogE.getLoginTime());
		loginLogDO.setTenantId(loginLogE.getTenantId());
		loginLogDO.setCreator(loginLogE.getCreator());
		loginLogDO.setEditor(loginLogE.getCreator());
		return loginLogDO;
	}

	public static LoginLogCO toClientObject(LoginEvent loginEvent) {
		LoginLogCO loginLogCO = new LoginLogCO();
		loginLogCO.setId(loginEvent.getId());
		loginLogCO.setUsername(loginEvent.getUsername());
		loginLogCO.setIp(loginEvent.getIp());
		loginLogCO.setAddress(loginEvent.getAddress());
		loginLogCO.setBrowser(loginEvent.getBrowser());
		loginLogCO.setOs(loginEvent.getOs());
		loginLogCO.setStatus(loginEvent.getStatus());
		loginLogCO.setErrorMessage(StringUtils.truncate(loginEvent.getErrorMessage(), 2000));
		loginLogCO.setType(loginEvent.getType());
		loginLogCO.setLoginTime(loginEvent.getLoginTime());
		loginLogCO.setTenantId(loginEvent.getTenantId());
		loginLogCO.setCreator(loginEvent.getCreator());
		return loginLogCO;
	}

	public static LoginEvent toDomainEvent(HttpServletRequest request, AuthA authA, BizException ex) throws Exception {
		Capabilities capabilities = RequestUtils.getCapabilities(request);
		String ip = IpUtils.getIpAddr(request);
		int status = LoginStatus.OK.getCode();
		UserE userE = authA.getUserE();
		Optional<UserE> optional = Optional.ofNullable(userE);
		Long creator = optional.map(UserE::getId).orElse(null);
		Long tenantId = optional.map(UserE::getTenantId).orElse(null);
		String errorMessage = StringConstants.EMPTY;
		if (ObjectUtils.isNotNull(ex)) {
			status = LoginStatus.FAIL.getCode();
			errorMessage = ex.getMsg();
		}
		return LoginEvent.builder()
			.id(authA.getId())
			.username(authA.getLoginName())
			.ip(ip)
			.address(AddressUtils.getRealAddress(ip))
			.browser(capabilities.getBrowser())
			.os(capabilities.getPlatform())
			.status(status)
			.errorMessage(errorMessage)
			.type(authA.getGrantType().getCode())
			.loginTime(authA.getCreateTime())
			.tenantId(tenantId)
			.creator(creator)
			.build();
	}

}
