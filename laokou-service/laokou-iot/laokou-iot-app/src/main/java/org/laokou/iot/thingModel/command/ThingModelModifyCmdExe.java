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

package org.laokou.iot.thingModel.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.iot.thingModel.dto.ThingModelModifyCmd;
import org.laokou.iot.thingModel.model.ThingModelE;
import org.springframework.stereotype.Component;
import org.laokou.iot.thingModel.convertor.ThingModelConvertor;
import org.laokou.iot.thingModel.ability.ThingModelDomainService;

import static org.laokou.common.tenant.constant.DSConstants.IOT;

/**
 *
 * 修改物模型命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ThingModelModifyCmdExe {

	private final ThingModelDomainService thingModelDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(ThingModelModifyCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(IOT);
			// 校验参数
			ThingModelE thingModelE = ThingModelConvertor.toEntity(cmd.getCo(), false);
			transactionalUtils.executeInTransaction(() -> thingModelDomainService.updateThingModel(thingModelE));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}

	}

}
