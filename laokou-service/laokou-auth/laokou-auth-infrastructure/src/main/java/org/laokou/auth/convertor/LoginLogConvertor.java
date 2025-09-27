/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.auth.model.LoginLogE;
import org.laokou.auth.model.LoginStatusEnum;
import org.laokou.auth.model.UserE;
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
		loginLogE.setLoginTime(co.getLoginTime());
		loginLogE.setTenantId(co.getTenantId());
		loginLogE.setUserId(co.getUserId());
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
		loginLogDO.setCreateTime(loginLogE.getLoginTime());
		loginLogDO.setUpdateTime(loginLogE.getLoginTime());
		loginLogDO.setTenantId(loginLogE.getTenantId());
		loginLogDO.setCreator(loginLogE.getUserId());
		loginLogDO.setEditor(loginLogE.getUserId());
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
		loginLogCO.setUserId(loginEvent.getUserId());
		return loginLogCO;
	}

	public static LoginEvent toDomainEvent(HttpServletRequest request, AuthA authA, BizException ex) throws Exception {
		Capabilities capabilities = RequestUtils.getCapabilities(request);
		String ip = IpUtils.getIpAddr(request);
		String address = AddressUtils.getRealAddress(ip);
		String os = capabilities.getPlatform();
		String browser = capabilities.getBrowser();
		int status = LoginStatusEnum.OK.getCode();
		UserE userE = authA.getUser();
		Optional<UserE> optional = Optional.ofNullable(userE);
		Long userId = optional.map(UserE::getId).orElse(null);
		Long tenantId = optional.map(UserE::getTenantId).orElse(null);
		String errorMessage = StringConstants.EMPTY;
		if (ObjectUtils.isNotNull(ex)) {
			status = LoginStatusEnum.FAIL.getCode();
			errorMessage = ex.getMsg();
		}
		return new LoginEvent(authA.getId(), authA.getLoginName(), ip, address, browser, os, status, errorMessage,
				authA.getGrantTypeEnum().getCode(), authA.getCreateTime(), tenantId, userId);
	}

}
