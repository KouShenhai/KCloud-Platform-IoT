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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询数据源下拉框选择项列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SourceOptionListQryExe {

	private final SourceMapper sourceMapper;

	/**
	 * 执行查询数据源下拉框选择项列表.
	 * @return 数据源下拉框选择项列表
	 */
	public Result<List<OptionCO>> execute() {
		List<SourceDO> list = sourceMapper.selectList(Wrappers.lambdaQuery(SourceDO.class)
			.select(SourceDO::getId, SourceDO::getName)
			.orderByDesc(SourceDO::getId));
		return Result.of(list.stream().map(this::convert).toList());
	}

	public OptionCO convert(SourceDO sourceDO) {
		return OptionCO.builder().value(String.valueOf(sourceDO.getId())).label(sourceDO.getName()).build();
	}

}
