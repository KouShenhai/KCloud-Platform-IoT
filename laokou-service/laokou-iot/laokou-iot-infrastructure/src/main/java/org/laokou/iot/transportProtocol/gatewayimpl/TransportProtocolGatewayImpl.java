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

package org.laokou.iot.transportProtocol.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.transportProtocol.model.TransportProtocolE;
import org.springframework.stereotype.Component;
import org.laokou.iot.transportProtocol.gateway.TransportProtocolGateway;
import org.laokou.iot.transportProtocol.gatewayimpl.database.TransportProtocolMapper;
import java.util.Arrays;
import org.laokou.iot.transportProtocol.convertor.TransportProtocolConvertor;
import org.laokou.iot.transportProtocol.gatewayimpl.database.dataobject.TransportProtocolDO;

/**
 *
 * 传输协议网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TransportProtocolGatewayImpl implements TransportProtocolGateway {

	private final TransportProtocolMapper transportProtocolMapper;

	@Override
	public void create(TransportProtocolE transportProtocolE) {
		transportProtocolMapper.insert(TransportProtocolConvertor.toDataObject(transportProtocolE, true));
	}

	@Override
	public void update(TransportProtocolE transportProtocolE) {
		TransportProtocolDO transportProtocolDO = TransportProtocolConvertor.toDataObject(transportProtocolE, false);
		transportProtocolDO.setVersion(transportProtocolMapper.selectVersion(transportProtocolE.getId()));
		transportProtocolMapper.updateById(transportProtocolDO);
	}

	@Override
	public void delete(Long[] ids) {
		transportProtocolMapper.deleteByIds(Arrays.asList(ids));
	}

}
