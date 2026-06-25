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

package org.laokou.admin.dict.convertor;

import org.laokou.admin.dict.dto.clientobject.DictCO;
import org.laokou.admin.dict.factory.DictDomainFactory;
import org.laokou.admin.dict.gatewayimpl.database.dataobject.DictDO;
import org.laokou.admin.dict.model.DictA;
import org.laokou.admin.dict.model.entity.DictE;

import java.util.List;

/**
 * 字典转换器.
 *
 * @author laokou
 */
public final class DictConvertor {

	private DictConvertor() {
	}

	public static DictDO toDataObject(DictA dictA) {
		DictE dictE = dictA.getDictE();
		DictDO dictDO = new DictDO();
		dictDO.setId(dictA.getId());
		dictDO.setCode(dictE.getCode());
		dictDO.setName(dictE.getName());
		dictDO.setRemark(dictE.getRemark());
		dictDO.setStatus(dictE.getStatus());
		dictDO.setCreateTime(dictA.getCreateTime());
		return dictDO;
	}

	public static DictCO toClientObject(DictDO dictDO) {
		DictCO dictCO = new DictCO();
		dictCO.setId(dictDO.getId());
		dictCO.setCode(dictDO.getCode());
		dictCO.setName(dictDO.getName());
		dictCO.setRemark(dictDO.getRemark());
		dictCO.setStatus(dictDO.getStatus());
		dictCO.setCreateTime(dictDO.getCreateTime());
		return dictCO;
	}

	public static List<DictCO> toClientObjectList(List<DictDO> list) {
		return list.stream().map(DictConvertor::toClientObject).toList();
	}

	public static DictE toEntity(DictCO dictCO) {
		return DictDomainFactory.createDictE()
			.toBuilder()
			.id(dictCO.getId())
			.code(dictCO.getCode())
			.name(dictCO.getName())
			.status(dictCO.getStatus())
			.remark(dictCO.getRemark())
			.build();
	}

}
