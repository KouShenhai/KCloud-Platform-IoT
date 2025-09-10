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

package org.laokou.iot.thingModel.command.query;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.thingModel.convertor.ThingModelConvertor;
import org.laokou.iot.thingModel.dto.ThingModelGetQry;
import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.springframework.stereotype.Component;

/**
 * 查看物模型请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ThingModelGetQryExe {

	private final ThingModelMapper thingModelMapper;

	public Result<ThingModelCO> execute(ThingModelGetQry qry) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			return Result.ok(ThingModelConvertor.toClientObject(thingModelMapper.selectById(qry.getId())));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
