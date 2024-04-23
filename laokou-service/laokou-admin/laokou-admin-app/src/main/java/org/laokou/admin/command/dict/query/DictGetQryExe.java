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

package org.laokou.admin.command.dict.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.dict.DictGetQry;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看字典执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictGetQryExe {

	private final DictMapper dictMapper;

	/**
	 * 执行查看字典.
	 * @param qry 查看字典参数
	 * @return 字典
	 */
	@DS(TENANT)
	public Result<DictCO> execute(DictGetQry qry) {
		return Result.ok(convert(dictMapper.selectById(qry.getId())));
	}

	private DictCO convert(DictDO dictDO) {
		return DictCO.builder()
			.id(dictDO.getId())
			.sort(dictDO.getSort())
			.remark(dictDO.getRemark())
			.value(dictDO.getValue())
			.label(dictDO.getLabel())
			.type(dictDO.getType())
			.build();
	}

}
