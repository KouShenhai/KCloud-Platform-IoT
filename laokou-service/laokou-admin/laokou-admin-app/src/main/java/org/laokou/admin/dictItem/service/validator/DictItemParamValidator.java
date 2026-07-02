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

package org.laokou.admin.dictItem.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.admin.dictItem.gatewayimpl.database.DictItemMapper;
import org.laokou.admin.dictItem.gatewayimpl.database.dataobject.DictItemDO;
import org.laokou.admin.dictItem.model.DictItemA;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * @author laokou
 */
final class DictItemParamValidator {

	private DictItemParamValidator() {
	}

	static ParamValidator.Validate validateId(DictItemA dictItemA) {
		Long id = dictItemA.getId();
		if (ObjectUtils.isNull(id)) {
			return ParamValidator.invalidate("字典项ID不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateCodeAndName(DictItemA dictItemA, DictItemMapper dictItemMapper) {
		String name = dictItemA.getDictItemE().getName();
		String code = dictItemA.getDictItemE().getCode();
		Long id = dictItemA.getId();
		if (StringExtUtils.isEmpty(code) || StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("字典项编码和字典项名称不能为空");
		}
		if (RegexUtils.matches("^[a-z]+(?:_[a-z]+)*$", code)) {
			return ParamValidator.invalidate("字典项编码只能使用小写字母和下划线，必须以小写字母开头和结尾，下划线不能连续");
		}
		if (dictItemA.isSave() && dictItemMapper.selectCount(Wrappers.lambdaQuery(DictItemDO.class)
			.eq(DictItemDO::getCode, code)
			.eq(DictItemDO::getName, name)) > 0) {
			return ParamValidator.invalidate("字典项编码和字典项名称已存在");
		}
		if (dictItemA.isModify() && dictItemMapper.selectCount(Wrappers.lambdaQuery(DictItemDO.class)
			.eq(DictItemDO::getCode, code)
			.eq(DictItemDO::getName, name)
			.ne(DictItemDO::getId, id)) > 0) {
			return ParamValidator.invalidate("字典项编码和字典项名称已存在");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateStatus(DictItemA dictItemA) {
		Integer status = dictItemA.getDictItemE().getStatus();
		if (status == null) {
			return ParamValidator.invalidate("字典项状态不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateSort(DictItemA dictItemA) {
		Integer sort = dictItemA.getDictItemE().getSort();
		if (ObjectUtils.isNull(sort)) {
			return ParamValidator.invalidate("字典项排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return ParamValidator.invalidate("字典项排序范围1-99999");
		}
		return ParamValidator.validate();
	}

}
