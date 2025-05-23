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

package org.laokou.iot.transportProtocol.command;

import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.annotation.CommandLog;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.laokou.iot.transportProtocol.dto.TransportProtocolModifyCmd;
import org.springframework.stereotype.Component;
import org.laokou.iot.transportProtocol.convertor.TransportProtocolConvertor;
import org.laokou.iot.transportProtocol.ability.TransportProtocolDomainService;

/**
 *
 * 修改传输协议命令执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TransportProtocolModifyCmdExe {

	private final TransportProtocolDomainService transportProtocolDomainService;

	private final TransactionalUtils transactionalUtils;

	@CommandLog
	public void executeVoid(TransportProtocolModifyCmd cmd) {
		// 校验参数
		transactionalUtils.executeInTransaction(() -> transportProtocolDomainService
			.updateTransportProtocol(TransportProtocolConvertor.toEntity(cmd.getCo())));
	}

}
