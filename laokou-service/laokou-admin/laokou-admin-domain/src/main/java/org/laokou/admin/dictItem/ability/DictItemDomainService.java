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

package org.laokou.admin.dictItem.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dictItem.gateway.DictItemGateway;
import org.laokou.admin.dictItem.model.DictItemE;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 字典项领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictItemDomainService {

	private final DictItemGateway dictItemGateway;

	public void createDictItem(DictItemE dictItemE) {
		checkDictItemParam(dictItemE, false);
		dictItemGateway.createDictItem(dictItemE);
	}

	public void updateDictItem(DictItemE dictItemE) {
		checkDictItemParam(dictItemE, true);
		dictItemGateway.updateDictItem(dictItemE);
	}

	public void deleteDictItem(Long[] ids) {
		checkRemoveParam(ids);
		dictItemGateway.deleteDictItem(ids);
	}

	private void checkDictItemParam(DictItemE dictItemE, boolean modify) {
		ParamValidator.validate("DictItem", validateCo(dictItemE), validateId(dictItemE, modify),
				validateLabel(dictItemE), validateValue(dictItemE), validateSort(dictItemE), validateRemark(dictItemE),
				validateStatus(dictItemE), validateTypeId(dictItemE), validateUniqueValue(dictItemE));
	}

	private void checkRemoveParam(Long[] ids) {
		ParamValidator.validate("DictItem", validateIds(ids));
	}

	private ParamValidator.Validate validateCo(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE)) {
			return ParamValidator.invalidate("字典项不能为空");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateId(DictItemE dictItemE, boolean modify) {
		if (ObjectUtils.isNull(dictItemE)) {
			return ParamValidator.validate();
		}
		if (modify && ObjectUtils.isNull(dictItemE.getId())) {
			return ParamValidator.invalidate("字典项ID不能为空");
		}
		if (modify && dictItemE.getId() < 1) {
			return ParamValidator.invalidate("字典项ID错误");
		}
		if (modify && !dictItemGateway.existsDictItem(dictItemE.getId())) {
			return ParamValidator.invalidate("字典项不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateLabel(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE)) {
			return ParamValidator.validate();
		}
		String label = dictItemE.getLabel();
		if (StringExtUtils.isEmpty(label)) {
			return ParamValidator.invalidate("字典标签不能为空");
		}
		if (label.length() > 100) {
			return ParamValidator.invalidate("字典标签不能超过100个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateValue(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE)) {
			return ParamValidator.validate();
		}
		String value = dictItemE.getValue();
		if (StringExtUtils.isEmpty(value)) {
			return ParamValidator.invalidate("字典值不能为空");
		}
		if (value.length() > 100) {
			return ParamValidator.invalidate("字典值不能超过100个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateSort(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE)) {
			return ParamValidator.validate();
		}
		Integer sort = dictItemE.getSort();
		if (ObjectUtils.isNull(sort)) {
			return ParamValidator.invalidate("字典排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return ParamValidator.invalidate("字典排序范围1-99999");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateRemark(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE) || StringExtUtils.isEmpty(dictItemE.getRemark())) {
			return ParamValidator.validate();
		}
		if (dictItemE.getRemark().length() > 500) {
			return ParamValidator.invalidate("字典备注不能超过500个字符");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateStatus(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE) || ObjectUtils.isNull(dictItemE.getStatus())) {
			return ParamValidator.invalidate("字典状态不能为空");
		}
		if (!List.of(0, 1).contains(dictItemE.getStatus())) {
			return ParamValidator.invalidate("字典状态不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateTypeId(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE) || ObjectUtils.isNull(dictItemE.getTypeId())) {
			return ParamValidator.invalidate("字典类型ID不能为空");
		}
		if (dictItemE.getTypeId() < 1) {
			return ParamValidator.invalidate("字典类型ID错误");
		}
		if (!dictItemGateway.existsDict(dictItemE.getTypeId())) {
			return ParamValidator.invalidate("字典类型不存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateUniqueValue(DictItemE dictItemE) {
		if (ObjectUtils.isNull(dictItemE) || ObjectUtils.isNull(dictItemE.getTypeId())
				|| StringExtUtils.isEmpty(dictItemE.getValue())) {
			return ParamValidator.validate();
		}
		if (dictItemGateway.existsValue(dictItemE.getId(), dictItemE.getTypeId(), dictItemE.getValue())) {
			return ParamValidator.invalidate("字典值已存在");
		}
		return ParamValidator.validate();
	}

	private ParamValidator.Validate validateIds(Long[] ids) {
		if (ObjectUtils.isNull(ids) || ids.length == 0 || CollectionExtUtils.isEmpty(Arrays.asList(ids))) {
			return ParamValidator.invalidate("字典项IDS不能为空");
		}
		if (Arrays.stream(ids).anyMatch(id -> ObjectUtils.isNull(id) || id < 1)) {
			return ParamValidator.invalidate("字典项IDS错误");
		}
		if (!dictItemGateway.existsDictItem(ids)) {
			return ParamValidator.invalidate("字典项不存在");
		}
		return ParamValidator.validate();
	}

}
