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

package org.laokou.admin.dictItem.convertor;

import org.laokou.admin.dictItem.dto.clientobject.DictItemCO;
import org.laokou.admin.dictItem.factory.DictItemDomainFactory;
import org.laokou.admin.dictItem.gatewayimpl.database.dataobject.DictItemDO;
import org.laokou.admin.dictItem.model.DictItemA;
import org.laokou.admin.dictItem.model.entity.DictItemE;

import java.util.List;

/**
 * 字典项转换器.
 *
 * @author laokou
 */
public final class DictItemConvertor {

	private DictItemConvertor() {
	}

	public static DictItemDO toDataObject(DictItemA dictItemA) {
		DictItemE dictItemE = dictItemA.getDictItemE();
		DictItemDO dictItemDO = new DictItemDO();
		dictItemDO.setId(dictItemA.getId());
		dictItemDO.setName(dictItemE.getName());
		dictItemDO.setCode(dictItemE.getCode());
		dictItemDO.setRemark(dictItemE.getRemark());
		dictItemDO.setStatus(dictItemE.getStatus());
		dictItemDO.setSort(dictItemE.getSort());
		dictItemDO.setDictId(dictItemE.getDictId());
		dictItemDO.setCreateTime(dictItemA.getCreateTime());
		return dictItemDO;
	}

	public static DictItemCO toClientObject(DictItemDO dictItemDO) {
		DictItemCO dictItemCO = new DictItemCO();
		dictItemCO.setId(dictItemDO.getId());
		dictItemCO.setName(dictItemDO.getName());
		dictItemCO.setCode(dictItemDO.getCode());
		dictItemCO.setRemark(dictItemDO.getRemark());
		dictItemCO.setStatus(dictItemDO.getStatus());
		dictItemCO.setSort(dictItemDO.getSort());
		dictItemCO.setDictId(dictItemDO.getDictId());
		dictItemCO.setCreateTime(dictItemDO.getCreateTime());
		return dictItemCO;
	}

	public static List<DictItemCO> toClientObjectList(List<DictItemDO> list) {
		return list.stream().map(DictItemConvertor::toClientObject).toList();
	}

	public static DictItemE toEntity(DictItemCO dictItemCO) {
		return DictItemDomainFactory.createDictItemE()
			.toBuilder()
			.id(dictItemCO.getId())
			.name(dictItemCO.getName())
			.code(dictItemCO.getCode())
			.status(dictItemCO.getStatus())
			.remark(dictItemCO.getRemark())
			.dictId(dictItemCO.getDictId())
			.sort(dictItemCO.getSort())
			.build();
	}

}
