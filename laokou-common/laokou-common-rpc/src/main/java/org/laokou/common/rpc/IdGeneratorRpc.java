/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.rpc;

import io.grpc.StatusException;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.grpc.annotation.GrpcClient;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.rpc.exception.GrpcNotFoundException;
import org.laokou.distributed.id.proto.DistributedIdServiceIGrpc;
import org.laokou.distributed.id.proto.GenerateBatchIdRequest;
import org.laokou.distributed.id.proto.GenerateBatchIdsResponse;
import org.laokou.distributed.id.proto.GenerateIdRequest;
import org.laokou.distributed.id.proto.GenerateIdResponse;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public class IdGeneratorRpc implements IdGenerator {

	@GrpcClient(serviceId = "laokou-distributed-id")
	private DistributedIdServiceIGrpc.DistributedIdServiceIBlockingV2Stub distributedIdServiceIBlockingV2Stub;

	@Override
	public Long getId() {
		try {
			GenerateIdResponse generateIdResponse = distributedIdServiceIBlockingV2Stub
				.generateId(GenerateIdRequest.newBuilder().build());
			return generateIdResponse.getData();
		}
		catch (StatusException ex) {
			log.error("生成分布式ID失败，错误信息：{}", ex.getMessage(), ex);
			throw new GrpcNotFoundException("B_Grpc_DistributedIdNotFound", "调用分布式ID服务失败，请联系管理员", ex);
		}
	}

	@Override
	public List<Long> getIds(int num) {
		try {
			GenerateBatchIdsResponse generateBatchIdsResponse = distributedIdServiceIBlockingV2Stub
				.generateBatchIds(GenerateBatchIdRequest.newBuilder().setNum(num).build());
			return generateBatchIdsResponse.getDataList();
		}
		catch (StatusException ex) {
			log.error("批量生成分布式ID失败，错误信息：{}", ex.getMessage(), ex);
			throw new GrpcNotFoundException("B_Grpc_DistributedIdNotFound", "调用分布式ID服务失败，请联系管理员", ex);
		}
	}

}
