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

package org.laokou.auth.gatewayimpl.rpc;

import org.apache.dubbo.config.annotation.DubboReference;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.distributed.identifier.api.DistributedIdentifierCmd;
import org.laokou.distributed.identifier.api.DistributedIdentifierResult;
import org.laokou.distributed.identifier.api.DistributedIdentifierServiceI;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
public class DistributedIdentifierRpc {

	@DubboReference(group = "iot-distributed-identifier", version = "v3",
			interfaceClass = DistributedIdentifierServiceI.class,
			mock = "org.laokou.auth.gatewayimpl.rpc.DistributedIdentifierMock", loadbalance = "adaptive", retries = 3)
	private DistributedIdentifierServiceI distributedIdentifierServiceI;

	public Long getId() {
		DistributedIdentifierResult result = distributedIdentifierServiceI
			.generateSnowflake(DistributedIdentifierCmd.newBuilder().build());
		if (ObjectUtils.equals(StatusCode.OK, result.getCode())) {
			return result.getData();
		}
		throw new BizException(result.getCode(), result.getMsg());
	}

}
