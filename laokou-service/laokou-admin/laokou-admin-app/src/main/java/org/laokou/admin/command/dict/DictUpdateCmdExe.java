/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import org.laokou.admin.dto.dict.DictUpdateCmd;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.common.BizCode;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.Constant.TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictUpdateCmdExe {

	private final DictGateway dictGateway;

	private final DictMapper dictMapper;

	@DS(TENANT)
	public Result<Boolean> execute(DictUpdateCmd cmd) {
		DictCO dictCO = cmd.getDictCO();
		Long id = dictCO.getId();
		if (id == null) {
			throw new GlobalException(BizCode.ID_NOT_NULL);
		}
		String type = dictCO.getType();
		String value = dictCO.getValue();
		Long count = dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getValue, value)
			.eq(DictDO::getType, type)
			.ne(DictDO::getId, dictCO.getId()));
		if (count > 0) {
			throw new GlobalException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
		}
		return Result.of(dictGateway.update(DictConvertor.toEntity(dictCO)));
	}

}
