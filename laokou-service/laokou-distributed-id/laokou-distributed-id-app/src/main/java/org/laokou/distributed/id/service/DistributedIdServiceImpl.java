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

package org.laokou.distributed.id.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.MessageUtils;
import org.laokou.distributed.id.proto.DistributedIdServiceIGrpc;
import org.laokou.distributed.id.proto.GenerateBatchIdRequest;
import org.laokou.distributed.id.proto.GenerateBatchIdsResponse;
import org.laokou.distributed.id.proto.GenerateIdRequest;
import org.laokou.distributed.id.proto.GenerateIdResponse;
import org.laokou.distributed.id.config.SnowflakeGenerator;
import org.laokou.distributed.id.config.SpringSnowflakeProperties;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class DistributedIdServiceImpl extends DistributedIdServiceIGrpc.DistributedIdServiceIImplBase {

	private final SnowflakeGenerator snowflakeGenerator;

	private final SpringSnowflakeProperties springSnowflakeProperties;

	@Override
	public void generateBatchIds(GenerateBatchIdRequest request,
			StreamObserver<GenerateBatchIdsResponse> responseObserver) {
		GenerateBatchIdsResponse.Builder builder = GenerateBatchIdsResponse.newBuilder()
			.setCode(StatusCode.OK)
			.setMsg(MessageUtils.getMessage(StatusCode.OK));
		int count = Math.min(springSnowflakeProperties.getMaxBatchSize(), request.getNum());
		for (int i = 0; i < count; i++) {
			builder.addData(snowflakeGenerator.nextId());
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void generateId(GenerateIdRequest request, StreamObserver<GenerateIdResponse> responseObserver) {
		GenerateIdResponse response = GenerateIdResponse.newBuilder()
			.setCode(StatusCode.OK)
			.setMsg(MessageUtils.getMessage(StatusCode.OK))
			.setData(snowflakeGenerator.nextId())
			.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}
