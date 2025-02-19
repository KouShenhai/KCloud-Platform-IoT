/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.communicationProtocol.ability;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.communicationProtocol.gateway.CommunicationProtocolGateway;
import org.laokou.iot.communicationProtocol.model.CommunicationProtocolE;
import org.springframework.stereotype.Component;

/**
 *
 * 通讯协议领域服务.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class CommunicationProtocolDomainService {

	private final CommunicationProtocolGateway communicationProtocolGateway;

	public void create(CommunicationProtocolE communicationProtocolE) {
		communicationProtocolGateway.create(communicationProtocolE);
	}

	public void update(CommunicationProtocolE communicationProtocolE) {
		communicationProtocolGateway.update(communicationProtocolE);
	}

	public void delete(Long[] ids) {
		communicationProtocolGateway.delete(ids);
	}

}
