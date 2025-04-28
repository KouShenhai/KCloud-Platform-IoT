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

package org.laokou.common.openfeign.rpc;

import org.laokou.common.i18n.dto.Result;
import org.laokou.common.openfeign.rpc.factory.DistributedIdentifierFeignClientFallbackFactory;
import org.laokou.distributed.identifier.dto.clientobject.DistributedIdentifierCO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import static org.laokou.common.openfeign.constant.FeignConstants.DISTRIBUTED_IDENTIFIER;
import static org.laokou.common.secret.aop.ApiSecretAop.*;

/**
 * @author laokou
 */
@FeignClient(value = DISTRIBUTED_IDENTIFIER, contextId = DISTRIBUTED_IDENTIFIER, path = "v3/distributed-identifiers",
		fallbackFactory = DistributedIdentifierFeignClientFallbackFactory.class)
public interface DistributedIdentifierFeignClient {

	@PostMapping("snowflake")
	Result<DistributedIdentifierCO> generateSnowflakeV3(@RequestHeader(APP_KEY) String appKey,
			@RequestHeader(APP_SECRET) String appSecret, @RequestHeader(TIMESTAMP) Long timestamp,
			@RequestHeader(NONCE) String nonce, @RequestHeader(SIGN) String sign);

}
