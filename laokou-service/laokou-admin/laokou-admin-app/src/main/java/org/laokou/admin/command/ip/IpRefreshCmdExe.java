/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.ip;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.IpConvertor;
import org.laokou.admin.domain.gateway.IpGateway;
import org.laokou.admin.domain.ip.Ip;
import org.laokou.admin.dto.ip.IpRefreshCmd;
import org.springframework.stereotype.Component;

/**
 * IP刷新至Redis执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class IpRefreshCmdExe {

	private final IpGateway ipGateway;

	private final IpConvertor ipConvertor;

	/**
	 * 执行IP刷新至Redis.
	 * @param cmd IP刷新至Redis参数
	 */
	public void executeVoid(IpRefreshCmd cmd) {
		ipGateway.refresh(new Ip(cmd.getLabel()));
	}

}
