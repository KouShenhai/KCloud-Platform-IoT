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

package org.laokou.iot.cp.convertor;

import org.laokou.iot.cp.gatewayimpl.database.dataobject.CpDO;
import org.laokou.iot.cp.dto.clientobject.CpCO;
import org.laokou.iot.cp.model.CpE;

/**
 *
 * 通讯协议转换器.
 *
 * @author laokou
 */
public class CpConvertor {

	public static CpDO toDataObject(CpE cpE, boolean isInsert) {
		CpDO cpDO = new CpDO();
		if (isInsert) {
			cpDO.generatorId();
		}
		else {
			cpDO.setId(cpE.getId());
		}
		cpDO.setName(cpE.getName());
		cpDO.setCode(cpE.getCode());
		cpDO.setSort(cpE.getSort());
		cpDO.setRemark(cpE.getRemark());
		return cpDO;
	}

	public static CpCO toClientObject(CpDO cpDO) {
		CpCO cpCO = new CpCO();
		cpCO.setName(cpDO.getName());
		cpCO.setCode(cpDO.getCode());
		cpCO.setSort(cpDO.getSort());
		cpCO.setRemark(cpDO.getRemark());
		return cpCO;
	}

	public static CpE toEntity(CpCO cpCO) {
		CpE cpE = new CpE();
		cpE.setName(cpCO.getName());
		cpE.setCode(cpCO.getCode());
		cpE.setSort(cpCO.getSort());
		cpE.setRemark(cpCO.getRemark());
		return cpE;
	}

}
