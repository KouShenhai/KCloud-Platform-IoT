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
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.thingModel.convertor.ThingModelConvertor;
import org.laokou.iot.thingModel.dto.ThingModelPageQry;
import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.tenant.constant.DSConstants.IOT;

/**
 * 分页物查询模型请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ThingModelPageQryExe {

	private final ThingModelMapper thingModelMapper;

	public Result<Page<ThingModelCO>> execute(ThingModelPageQry qry) {
		try {
			DynamicDataSourceContextHolder.push(IOT);
			List<ThingModelDO> list = thingModelMapper.selectObjectPage(qry);
			long total = thingModelMapper.selectObjectCount(qry);
			return Result.ok(Page.create(ThingModelConvertor.toClientObjects(list), total));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
