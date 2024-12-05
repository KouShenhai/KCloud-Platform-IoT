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

import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.LogV;

/**
 * @author laokou
 */
public class LoginLogConvertor {

	public static LoginEvent toEvent(AuthA authA) {
		LogV logV = authA.getLog();
		// return new LoginEvent(logV.type(), logV.errorMessage(), logV.status(),
		// logV.browser(), logV.os(), logV.ip(),
		// logV.address(), logV.username(), authA.getInstant(),
		// authA.getUser().getTenantId(), LAOKOU_LOG_TOPIC,
		// LOGIN_TAG);
		return null;
	}

	public static LoginLogDO toDataObject(LoginEvent loginEvent) {
		LoginLogDO loginLogDO = new LoginLogDO();
		loginLogDO.setId(loginEvent.getId());
		// loginLogDO.setCreator(loginEvent.getCreator());
		// loginLogDO.setEditor(loginEvent.getCreator());
		// loginLogDO.setCreateTime(loginEvent.getCreateTime());
		// loginLogDO.setUpdateTime(loginEvent.getCreateTime());
		loginLogDO.setTenantId(loginEvent.getTenantId());
		loginLogDO.setUsername(loginEvent.getUsername());
		loginLogDO.setIp(loginEvent.getIp());
		loginLogDO.setAddress(loginEvent.getAddress());
		loginLogDO.setBrowser(loginEvent.getBrowser());
		loginLogDO.setOs(loginEvent.getOs());
		loginLogDO.setStatus(loginEvent.getStatus());
		loginLogDO.setErrorMessage(loginEvent.getErrorMessage());
		loginLogDO.setType(loginEvent.getType());
		return loginLogDO;
	}

}
