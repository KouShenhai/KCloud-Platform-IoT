/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.source.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.source.SourceGetQry;
import org.laokou.admin.dto.source.clientobject.SourceCO;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查看数据源执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceGetQryExe {

	private final SourceMapper sourceMapper;

	/**
	 * 执行查看数据源.
	 * @param qry 查看数据源参数
	 * @return 数据源
	 */
	public Result<SourceCO> execute(SourceGetQry qry) {
		return Result.of(convert(sourceMapper.selectById(qry.getId())));
	}

	private SourceCO convert(SourceDO sourceDO) {
		return SourceCO.builder()
			.id(sourceDO.getId())
			.name(sourceDO.getName())
			.url(sourceDO.getUrl())
			.driverClassName(sourceDO.getDriverClassName())
			.username(sourceDO.getUsername())
			.password(sourceDO.getPassword())
			.build();
	}

}
