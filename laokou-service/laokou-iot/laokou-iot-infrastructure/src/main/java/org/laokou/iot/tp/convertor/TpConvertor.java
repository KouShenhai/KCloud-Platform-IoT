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

package org.laokou.iot.tp.convertor;

import org.laokou.iot.tp.gatewayimpl.database.dataobject.TpDO;
import org.laokou.iot.tp.dto.clientobject.TpCO;
import org.laokou.iot.tp.model.TpE;

/**
 *
 * 传输协议转换器.
 *
 * @author laokou
 */
public class TpConvertor {

	public static TpDO toDataObject(TpE tpE, boolean isInsert) {
		TpDO tpDO = new TpDO();
		if (isInsert) {
			tpDO.generatorId();
		}
		else {
			tpDO.setId(tpE.getId());
		}
		tpDO.setName(tpE.getName());
		tpDO.setType(tpE.getType());
		tpDO.setHost(tpE.getHost());
		tpDO.setPort(tpE.getPort());
		tpDO.setClientId(tpE.getClientId());
		tpDO.setTopic(tpE.getTopic());
		tpDO.setUsername(tpE.getUsername());
		tpDO.setPassword(tpE.getPassword());
		tpDO.setRemark(tpE.getRemark());
		return tpDO;
	}

	public static TpCO toClientObject(TpDO tpDO) {
		TpCO tpCO = new TpCO();
		tpCO.setName(tpDO.getName());
		tpCO.setType(tpDO.getType());
		tpCO.setHost(tpDO.getHost());
		tpCO.setPort(tpDO.getPort());
		tpCO.setClientId(tpDO.getClientId());
		tpCO.setTopic(tpDO.getTopic());
		tpCO.setUsername(tpDO.getUsername());
		tpCO.setPassword(tpDO.getPassword());
		tpCO.setRemark(tpDO.getRemark());
		return tpCO;
	}

	public static TpE toEntity(TpCO tpCO) {
		TpE tpE = new TpE();
		tpE.setName(tpCO.getName());
		tpE.setType(tpCO.getType());
		tpE.setHost(tpCO.getHost());
		tpE.setPort(tpCO.getPort());
		tpE.setClientId(tpCO.getClientId());
		tpE.setTopic(tpCO.getTopic());
		tpE.setUsername(tpCO.getUsername());
		tpE.setPassword(tpCO.getPassword());
		tpE.setRemark(tpCO.getRemark());
		return tpE;
	}

}
