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

package org.laokou.distributed.identifier.service;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.laokou.common.i18n.dto.Result;
import org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd;
import org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult;
import org.laokou.distributed.identifier.api.DistributedIdentifierCmd;
import org.laokou.distributed.identifier.api.DistributedIdentifierResult;
import org.laokou.distributed.identifier.api.DubboDistributedIdentifierServiceITriple;
import org.laokou.distributed.identifier.command.DistributedIdentifierGenerateBatchCmdExe;
import org.laokou.distributed.identifier.command.DistributedIdentifierGenerateCmdExe;
import org.laokou.distributed.identifier.dto.DistributedIdentifierGenerateBatchCmd;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@DubboService(token = "3f7c5e45d6a7", group = "iot-distributed-identifier", version = "v3", timeout = 10000)
@RequiredArgsConstructor
public class DistributedIdentifierServiceImpl
		extends DubboDistributedIdentifierServiceITriple.DistributedIdentifierServiceIImplBase {

	private final DistributedIdentifierGenerateCmdExe distributedIdentifierGenerateCmdExe;

	private final DistributedIdentifierGenerateBatchCmdExe distributedIdentifierGenerateBatchCmdExe;

	@Override
	public DistributedIdentifierResult generateSnowflake(DistributedIdentifierCmd cmd) {
		Result<Long> result = distributedIdentifierGenerateCmdExe.execute();
		return DistributedIdentifierResult.newBuilder()
			.setCode(result.getCode())
			.setMsg(result.getMsg())
			.setData(result.getData())
			.build();
	}

	@Override
	public DistributedIdentifierBatchResult generateSnowflakeBatch(DistributedIdentifierBatchCmd cmd) {
		Result<List<Long>> result = distributedIdentifierGenerateBatchCmdExe
			.execute(new DistributedIdentifierGenerateBatchCmd(cmd.getNum()));
		return DistributedIdentifierBatchResult.newBuilder()
			.setCode(result.getCode())
			.setMsg(result.getMsg())
			.addAllData(result.getData())
			.build();
	}

}
