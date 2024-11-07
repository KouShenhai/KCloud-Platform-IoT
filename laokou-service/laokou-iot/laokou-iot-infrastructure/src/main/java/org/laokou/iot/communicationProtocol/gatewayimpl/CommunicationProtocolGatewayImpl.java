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

package org.laokou.iot.communicationProtocol.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.communicationProtocol.model.CommunicationProtocolE;
import org.springframework.stereotype.Component;
import org.laokou.iot.communicationProtocol.gateway.CommunicationProtocolGateway;
import org.laokou.iot.communicationProtocol.gatewayimpl.database.CommunicationProtocolMapper;
import java.util.Arrays;
import org.laokou.iot.communicationProtocol.convertor.CommunicationProtocolConvertor;
import org.laokou.iot.communicationProtocol.gatewayimpl.database.dataobject.CommunicationProtocolDO;

/**
 *
 * 通讯协议网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CommunicationProtocolGatewayImpl implements CommunicationProtocolGateway {

	private final CommunicationProtocolMapper communicationProtocolMapper;

	@Override
	public void create(CommunicationProtocolE communicationProtocolE) {
		communicationProtocolMapper.insert(CommunicationProtocolConvertor.toDataObject(communicationProtocolE, true));
	}

	@Override
	public void update(CommunicationProtocolE communicationProtocolE) {
		CommunicationProtocolDO communicationProtocolDO = CommunicationProtocolConvertor
			.toDataObject(communicationProtocolE, false);
		communicationProtocolDO.setVersion(communicationProtocolMapper.selectVersion(communicationProtocolE.getId()));
		communicationProtocolMapper.updateById(communicationProtocolDO);
	}

	@Override
	public void delete(Long[] ids) {
		communicationProtocolMapper.deleteByIds(Arrays.asList(ids));
	}

}
