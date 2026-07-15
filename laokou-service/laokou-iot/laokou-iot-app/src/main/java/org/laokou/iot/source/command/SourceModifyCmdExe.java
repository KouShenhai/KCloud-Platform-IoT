/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.source.command;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.source.ability.SourceDomainService;
import org.laokou.iot.source.convertor.SourceConvertor;
import org.laokou.iot.source.dto.SourceModifyCmd;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.iot.source.factory.SourceDomainFactory;
import org.laokou.iot.source.model.SourceA;
import org.springframework.stereotype.Component;

/**
 * 修改数据源命令执行器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceModifyCmdExe {

	private final SourceDomainService sourceDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(SourceModifyCmd cmd) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			SourceA sourceA = SourceDomainFactory.createSourceA().create(SourceConvertor.toEntity(cmd.getCo()));
			// 校验参数
			sourceA.checkSourceParam();
			transactionalUtils.executeInTransaction(() -> sourceDomainService.updateSource(sourceA));
		}
		catch (Exception ex) {
			log.error("保存数据源失败，错误信息：{}", ex.getMessage(), ex);
			throw ex;
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
