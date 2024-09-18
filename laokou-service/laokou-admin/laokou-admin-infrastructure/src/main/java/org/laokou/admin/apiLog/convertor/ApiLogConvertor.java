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

package org.laokou.admin.apiLog.convertor;

import org.laokou.admin.apiLog.dto.clientobject.ApiLogCO;
import org.laokou.admin.apiLog.gatewayimpl.database.dataobject.ApiLogDO;
import org.laokou.admin.apiLog.model.ApiLogE;

/**
 * Api日志转换器.
 *
 * @author laokou
 */
public class ApiLogConvertor {

	public static ApiLogDO toDataObject(ApiLogE apiLogE, boolean isInsert) {
		ApiLogDO apiLogDO = new ApiLogDO();
		if (isInsert) {
			apiLogDO.generatorId();
		}
		else {
			apiLogDO.setId(apiLogE.getId());
		}
		apiLogDO.setCode(apiLogE.getCode());
		apiLogDO.setName(apiLogE.getName());
		apiLogDO.setStatus(apiLogE.getStatus());
		apiLogDO.setErrorMessage(apiLogE.getErrorMessage());
		apiLogDO.setParam(apiLogE.getParam());
		return apiLogDO;
	}

	public static ApiLogCO toClientObject(ApiLogDO apiLogDO) {
		ApiLogCO apiLogCO = new ApiLogCO();
		apiLogCO.setId(apiLogDO.getId());
		apiLogCO.setCode(apiLogDO.getCode());
		apiLogCO.setName(apiLogDO.getName());
		apiLogCO.setStatus(apiLogDO.getStatus());
		apiLogCO.setErrorMessage(apiLogDO.getErrorMessage());
		apiLogCO.setParam(apiLogDO.getParam());
		apiLogCO.setCreateTime(apiLogDO.getCreateTime());
		return apiLogCO;
	}

	public static ApiLogE toEntity(ApiLogCO apiLogCO) {
		ApiLogE apiLogE = new ApiLogE();
		apiLogE.setId(apiLogCO.getId());
		apiLogE.setCode(apiLogCO.getCode());
		apiLogE.setName(apiLogCO.getName());
		apiLogE.setStatus(apiLogCO.getStatus());
		apiLogE.setErrorMessage(apiLogCO.getErrorMessage());
		apiLogE.setParam(apiLogCO.getParam());
		return apiLogE;
	}

}
