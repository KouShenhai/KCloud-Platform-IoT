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
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * Api日志转换器.
 *
 * @author laokou
 */
public class ApiLogConvertor {

	public static ApiLogDO toDataObject(ApiLogE apiLogE) {
		ApiLogDO apiLogDO = ConvertUtil.sourceToTarget(apiLogE, ApiLogDO.class);
		if (ObjectUtil.isNull(apiLogDO.getId())) {
			apiLogDO.generatorId();
		}
		return apiLogDO;
	}

	public static ApiLogCO toClientObject(ApiLogDO apiLogDO) {
		return ConvertUtil.sourceToTarget(apiLogDO, ApiLogCO.class);
	}

	public static ApiLogE toEntity(ApiLogCO apiLogCO) {
		return ConvertUtil.sourceToTarget(apiLogCO, ApiLogE.class);
	}

}
