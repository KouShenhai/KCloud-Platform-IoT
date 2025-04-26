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

package org.laokou.common.openfeign.rpc.fallback;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.openfeign.rpc.DistributedIdentifierFeignClient;
import org.laokou.distributed.identifier.dto.clientobject.DistributedIdentifierCO;

/**
 * @author laokou
 */
@Slf4j
public class DistributedIdentifierFeignClientFallback implements DistributedIdentifierFeignClient {

	private final Throwable cause;

	public DistributedIdentifierFeignClientFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public Result<DistributedIdentifierCO> generateSnowflakeV3() {
		log.error("分布式ID调用失败，错误信息：{}", cause.getMessage(), cause);
		return Result.ok(new DistributedIdentifierCO(System.currentTimeMillis(),
				DateUtils.format(DateUtils.now(), DateUtils.YYYY_B_MM_B_DD_HH_R_MM_R_SS_D_SSS)));
	}

}
