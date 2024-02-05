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

package org.laokou.admin.command.dict;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.dto.dict.DictModifyCmd;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.ValCodes.SYSTEM_ID_REQUIRE;
import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 修改字典执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictModifyCmdExe {

	private final DictGateway dictGateway;

	private final DictMapper dictMapper;

	private final DictConvertor dictConvertor;

	/**
	 * 执行修改字典.
	 * @param cmd 修改字典参数
	 * @return 执行修改结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(DictModifyCmd cmd) {
		DictCO co = cmd.getDictCO();
		Long id = co.getId();
		if (ObjectUtil.isNull(id)) {
			throw new SystemException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
		String type = co.getType();
		String value = co.getValue();
		Long count = dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getValue, value)
			.eq(DictDO::getType, type)
			.ne(DictDO::getId, co.getId()));
		if (count > 0) {
			throw new SystemException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
		}
		return null;
		// return Result.of(dictGateway.update(dictConvertor.toEntity(co)));
	}

}
