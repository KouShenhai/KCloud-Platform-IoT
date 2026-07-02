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

package org.laokou.admin.dict.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.dict.gatewayimpl.database.DictMapper;
import org.laokou.admin.dict.gatewayimpl.database.dataobject.DictDO;
import org.laokou.admin.dict.model.DictA;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * @author laokou
 */
final class DictParamValidator {

	private DictParamValidator() {
	}

	static ParamValidator.Validate validateId(DictA dictA) {
		Long id = dictA.getId();
		if (ObjectUtils.isNull(id)) {
			return ParamValidator.invalidate("字典ID不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateCodeAndName(DictA dictA, DictMapper dictMapper) {
		String name = dictA.getDictE().getName();
		String code = dictA.getDictE().getCode();
		Long id = dictA.getId();
		if (StringExtUtils.isEmpty(code) || StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("字典编码和字典名称不能为空");
		}
		if (RegexUtils.matches("^[a-z]+(?:_[a-z]+)*$", code)) {
			return ParamValidator.invalidate("字典编码只能使用小写字母和下划线，必须以小写字母开头和结尾，下划线不能连续");
		}
		if (dictA.isSave() && dictMapper
			.selectCount(Wrappers.lambdaQuery(DictDO.class).eq(DictDO::getCode, code).eq(DictDO::getName, name)) > 0) {
			return ParamValidator.invalidate("字典编码和字典名称已存在");
		}
		if (dictA.isModify() && dictMapper.selectCount(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getCode, code)
			.eq(DictDO::getName, name)
			.ne(DictDO::getId, id)) > 0) {
			return ParamValidator.invalidate("字典编码和字典名称已存在");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateStatus(DictA dictA) {
		Integer status = dictA.getDictE().getStatus();
		if (status == null) {
			return ParamValidator.invalidate("字典状态不能为空");
		}
		return ParamValidator.validate();
	}

}
