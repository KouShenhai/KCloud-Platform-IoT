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

package org.laokou.admin.dict.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dict.gateway.DictGateway;
import org.laokou.admin.dict.model.DictE;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 字典领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictDomainService {

	private final DictGateway dictGateway;

	public void createDict(DictE dictE) {
		checkDictParam(dictE, false);
		dictGateway.createDict(dictE);
	}

	public void updateDict(DictE dictE) {
		checkDictParam(dictE, true);
		dictGateway.updateDict(dictE);
	}

	public void deleteDict(Long[] ids) {
		checkRemoveParam(ids);
		dictGateway.deleteDict(ids);
	}

	private void checkDictParam(DictE dictE, boolean modify) {
		ParamValidator.validate("Dict", validateCo(dictE), validateId(dictE, modify), validateName(dictE),
				validateType(dictE), validateRemark(dictE), validateStatus(dictE), validateUniqueType(dictE));
	}

	private void checkRemoveParam(Long[] ids) {
		ParamValidator.validate("Dict", validateIds(ids), validateDeleteProtection(ids));
	}

	private ParamValidator.Validate validateCo(DictE dictE) {
		if (ObjectUtils.isNull(dictE)) {
			return ParamValidator.invalidate("字典不能为空");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateId(DictE dictE, boolean modify) {
		if (ObjectUtils.isNull(dictE)) {
			return ParamValidator.validate();
		}
		if (modify && ObjectUtils.isNull(dictE.getId())) {
			return ParamValidator.invalidate("字典ID不能为空");
		}
		if (modify && dictE.getId() < 1) {
			return ParamValidator.invalidate("字典ID错误");
		}
		if (modify && !dictGateway.existsDict(dictE.getId())) {
			return ParamValidator.invalidate("字典不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateName(DictE dictE) {
		if (ObjectUtils.isNull(dictE)) {
			return ParamValidator.validate();
		}
		String name = dictE.getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("字典名称不能为空");
		}
		if (name.length() > 100) {
			return ParamValidator.invalidate("字典名称不能超过100个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateType(DictE dictE) {
		if (ObjectUtils.isNull(dictE)) {
			return ParamValidator.validate();
		}
		String type = dictE.getType();
		if (StringExtUtils.isEmpty(type)) {
			return ParamValidator.invalidate("字典类型不能为空");
		}
		if (type.length() > 100) {
			return ParamValidator.invalidate("字典类型不能超过100个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateRemark(DictE dictE) {
		if (ObjectUtils.isNull(dictE) || StringExtUtils.isEmpty(dictE.getRemark())) {
			return ParamValidator.validate();
		}
		if (dictE.getRemark().length() > 500) {
			return ParamValidator.invalidate("字典备注不能超过500个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateStatus(DictE dictE) {
		if (ObjectUtils.isNull(dictE) || ObjectUtils.isNull(dictE.getStatus())) {
			return ParamValidator.invalidate("字典状态不能为空");
		}
		if (!List.of(0, 1).contains(dictE.getStatus())) {
			return ParamValidator.invalidate("字典状态不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateUniqueType(DictE dictE) {
		if (ObjectUtils.isNull(dictE) || StringExtUtils.isEmpty(dictE.getType())) {
			return ParamValidator.validate();
		}
		if (dictGateway.existsType(dictE.getId(), dictE.getType())) {
			return ParamValidator.invalidate("字典类型已存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateIds(Long[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return ParamValidator.invalidate("字典IDS不能为空");
		}
		if (Arrays.stream(ids).anyMatch(id -> ObjectUtils.isNull(id) || id < 1)) {
			return ParamValidator.invalidate("字典IDS错误");
		}
		if (!dictGateway.existsDict(ids)) {
			return ParamValidator.invalidate("字典不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateDeleteProtection(Long[] ids) {
		if (ObjectUtils.isNotNull(ids) && ids.length > 0 && dictGateway.existsDictItem(ids)) {
			return ParamValidator.invalidate("字典存在字典项，请先删除或停用字典项");
		}
		return ParamValidator.validate();
	}

}
