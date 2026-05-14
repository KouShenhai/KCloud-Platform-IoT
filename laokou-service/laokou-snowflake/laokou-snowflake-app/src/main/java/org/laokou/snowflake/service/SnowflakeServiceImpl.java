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

package org.laokou.snowflake.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.MessageUtils;
import org.laokou.snowflake.config.IdGenerator;
import org.springframework.grpc.server.service.GrpcService;

/**
 * 分布式 ID gRPC 服务实现.
 *
 * @author laokou
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class SnowflakeServiceImpl extends SnowflakeServiceIGrpc.SnowflakeServiceIImplBase {

	private final IdGenerator idGenerator;

	@Override
	public void generateId(GenerateIdRequest request, StreamObserver<GenerateIdResponse> responseObserver) {
		GenerateIdResponse response = GenerateIdResponse.newBuilder()
			.setCode(StatusCode.OK)
			.setMsg(MessageUtils.getMessage(StatusCode.OK))
			.setData(idGenerator.nextId())
			.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void generateBatchIds(GenerateBatchIdsRequest request,
			StreamObserver<GenerateBatchIdsResponse> responseObserver) {
		GenerateBatchIdsResponse.Builder builder = GenerateBatchIdsResponse.newBuilder()
			.setCode(StatusCode.OK)
			.setMsg(MessageUtils.getMessage(StatusCode.OK));
		responseObserver.onNext(builder.addAllData(idGenerator.nextIds(request.getNum())).build());
		responseObserver.onCompleted();
	}

}
