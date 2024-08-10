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

import org.laokou.auth.dto.domainevent.CallApiEvent;
import org.laokou.auth.gatewayimpl.database.dataobject.ApiLogDO;

/**
 * @author laokou
 */
public class ApiLogConvertor {

	public static ApiLogDO toDataObject(CallApiEvent callApiEvent) {
		ApiLogDO apiLogDO = new ApiLogDO();
		apiLogDO.setId(callApiEvent.getId());
		apiLogDO.setCreator(callApiEvent.getCreator());
		apiLogDO.setEditor(callApiEvent.getEditor());
		apiLogDO.setCreateDate(callApiEvent.getCreateDate());
		apiLogDO.setUpdateDate(callApiEvent.getUpdateDate());
		apiLogDO.setDeptId(callApiEvent.getDeptId());
		apiLogDO.setDeptPath(callApiEvent.getDeptPath());
		apiLogDO.setTenantId(callApiEvent.getTenantId());
		apiLogDO.setCode(callApiEvent.getCode());
		apiLogDO.setName(callApiEvent.getName());
		apiLogDO.setStatus(callApiEvent.getStatus());
		apiLogDO.setErrorMessage(callApiEvent.getErrorMessage());
		apiLogDO.setParam(callApiEvent.getParam());
		return apiLogDO;
	}

}
