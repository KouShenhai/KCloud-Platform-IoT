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

package org.laokou.admin.loginLog.convertor;

import org.laokou.admin.loginLog.dto.clientobject.LoginLogCO;
import org.laokou.admin.loginLog.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.admin.loginLog.model.LoginLogE;

/**
 * 登录日志转换器.
 *
 * @author laokou
 */
public class LoginLogConvertor {

	public static LoginLogDO toDataObject(LoginLogE loginLogE, boolean isInsert) {
		LoginLogDO loginLogDO = new LoginLogDO();
		if (isInsert) {
			loginLogDO.generatorId();
		}
		else {
			loginLogDO.setId(loginLogE.getId());
		}
		loginLogDO.setUsername(loginLogE.getUsername());
		loginLogDO.setIp(loginLogE.getIp());
		loginLogDO.setAddress(loginLogE.getAddress());
		loginLogDO.setBrowser(loginLogE.getBrowser());
		loginLogDO.setOs(loginLogE.getOs());
		loginLogDO.setStatus(loginLogE.getStatus());
		loginLogDO.setErrorMessage(loginLogE.getErrorMessage());
		loginLogDO.setType(loginLogE.getType());
		return loginLogDO;
	}

	public static LoginLogCO toClientObject(LoginLogDO loginLogDO) {
		LoginLogCO loginLogCO = new LoginLogCO();
		loginLogCO.setId(loginLogDO.getId());
		loginLogCO.setUsername(loginLogDO.getUsername());
		loginLogCO.setIp(loginLogDO.getIp());
		loginLogCO.setAddress(loginLogDO.getAddress());
		loginLogCO.setBrowser(loginLogDO.getBrowser());
		loginLogCO.setOs(loginLogDO.getOs());
		loginLogCO.setStatus(loginLogDO.getStatus());
		loginLogCO.setErrorMessage(loginLogDO.getErrorMessage());
		loginLogCO.setType(loginLogDO.getType());
		loginLogCO.setCreateTime(loginLogDO.getCreateTime());
		return loginLogCO;
	}

	public static LoginLogE toEntity(LoginLogCO loginLogCO) {
		LoginLogE loginLogE = new LoginLogE();
		loginLogE.setId(loginLogCO.getId());
		loginLogE.setUsername(loginLogCO.getUsername());
		loginLogE.setIp(loginLogCO.getIp());
		loginLogE.setAddress(loginLogCO.getAddress());
		loginLogE.setBrowser(loginLogCO.getBrowser());
		loginLogE.setOs(loginLogCO.getOs());
		loginLogE.setStatus(loginLogCO.getStatus());
		loginLogE.setErrorMessage(loginLogCO.getErrorMessage());
		loginLogE.setType(loginLogCO.getType());
		return loginLogE;
	}

}
