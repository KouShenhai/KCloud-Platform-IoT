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

import lombok.RequiredArgsConstructor;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.iot.thingModel.dto.ThingModelModifyCmd;
import org.springframework.stereotype.Component;
import org.laokou.iot.thingModel.convertor.ThingModelConvertor;
import org.laokou.iot.thingModel.ability.ThingModelDomainService;

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

	private final TransactionalUtil transactionalUtil;

	public void executeVoid(ThingModelModifyCmd cmd) {
		// 校验参数
		transactionalUtil
			.executeInTransaction(() -> thingModelDomainService.update(ThingModelConvertor.toEntity(cmd.getCo())));
	}

}
