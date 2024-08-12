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

package org.laokou.admin.command.dict.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Option;
import org.laokou.admin.dto.dict.DictOptionListQry;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictTypeDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.admin.config.DsTenantProcessor.TENANT;

/**
 * 查询字典下拉框选择项列表执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictOptionListQryExe {

	private final DictMapper dictMapper;

	/**
	 * 执行查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	@DS(TENANT)
	public Result<List<Option>> execute(DictOptionListQry qry) {
		List<DictTypeDO> list = dictMapper.selectList(Wrappers.lambdaQuery(DictTypeDO.class)
			.eq(DictTypeDO::getType, qry.getType())
			// .select(DictTypeDO::getLabel, DictTypeDO::getValue)
			.orderByDesc(DictTypeDO::getId));
		return null;
		// return Result.ok(list.stream().map(dict -> new Option(dict.getLabel(),
		// dict.getValue())).toList());
	}

}
