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
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * 登录日志转换器.
 *
 * @author laokou
 */
public class LoginLogConvertor {

	public static LoginLogDO toDataObject(LoginLogE loginLogE) {
		LoginLogDO loginLogDO = ConvertUtil.sourceToTarget(loginLogE, LoginLogDO.class);
		if (ObjectUtil.isNull(loginLogDO.getId())) {
			loginLogDO.generatorId();
		}
		return loginLogDO;
	}

	public static LoginLogCO toClientObject(LoginLogDO loginLogDO) {
		return ConvertUtil.sourceToTarget(loginLogDO, LoginLogCO.class);
	}

	public static LoginLogE toEntity(LoginLogCO loginLogCO) {
		return ConvertUtil.sourceToTarget(loginLogCO, LoginLogE.class);
	}

}
