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

package org.laokou.iot.source.convertor;

import org.laokou.iot.source.dto.clientobject.SourceCO;
import org.laokou.iot.source.factory.SourceDomainFactory;
import org.laokou.iot.source.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.iot.source.model.SourceA;
import org.laokou.iot.source.model.entity.SourceE;

import java.util.List;

/**
 * 数据源转换器.
 *
 * @author laokou
 */
public final class SourceConvertor {

	private SourceConvertor() {
	}

	public static SourceDO toDataObject(SourceA sourceA) {
		SourceDO sourceDO = new SourceDO();
		SourceE sourceE = sourceA.getSourceE();
		sourceDO.setId(sourceA.getId());
		sourceDO.setName(sourceE.getName());
		sourceDO.setUsername(sourceE.getUsername());
		sourceDO.setPassword(sourceE.getPassword());
		sourceDO.setEndpoint(sourceE.getEndpoint());
		sourceDO.setType(sourceE.getType());
		sourceDO.setDbName(sourceE.getDbName());
		return sourceDO;
	}

	public static List<SourceCO> toClientObjects(List<SourceDO> list) {
		return list.stream().map(SourceConvertor::toClientObject).toList();
	}

	public static SourceCO toClientObject(SourceDO sourceDO) {
		SourceCO sourceCO = new SourceCO();
		sourceCO.setId(sourceDO.getId());
		sourceCO.setName(sourceDO.getName());
		sourceCO.setUsername(sourceDO.getUsername());
		sourceCO.setPassword(sourceDO.getPassword());
		sourceCO.setEndpoint(sourceDO.getEndpoint());
		sourceCO.setType(sourceDO.getType());
		sourceCO.setDbName(sourceDO.getDbName());
		sourceCO.setCreateTime(sourceDO.getCreateTime());
		return sourceCO;
	}

	public static SourceE toEntity(SourceCO sourceCO) {
		return SourceDomainFactory.createSourceE()
			.toBuilder()
			.id(sourceCO.getId())
			.name(sourceCO.getName())
			.username(sourceCO.getUsername())
			.password(sourceCO.getPassword())
			.endpoint(sourceCO.getEndpoint())
			.dbName(sourceCO.getDbName())
			.type(sourceCO.getType())
			.build();
	}

}
