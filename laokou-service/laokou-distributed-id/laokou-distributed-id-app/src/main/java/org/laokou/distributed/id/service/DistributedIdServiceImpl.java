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
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.MessageUtils;
import org.laokou.distributed.id.config.IdGenerator;
import org.laokou.distributed.id.config.SpringSnowflakeProperties;
import org.laokou.distributed.id.proto.DistributedIdServiceIGrpc;
import org.laokou.distributed.id.proto.GenerateBatchIdRequest;
import org.laokou.distributed.id.proto.GenerateBatchIdsResponse;
import org.laokou.distributed.id.proto.GenerateIdRequest;
import org.laokou.distributed.id.proto.GenerateIdResponse;
import org.laokou.distributed.id.proto.IdType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式 ID gRPC 服务实现.
 * <p>
 * 根据请求中的 {@link IdType} 参数动态选择 ID 生成策略（雪花算法 / Redis 分段）。
 * </p>
 *
 * @author laokou
 */
@Slf4j
@GrpcService
public class DistributedIdServiceImpl extends DistributedIdServiceIGrpc.DistributedIdServiceIImplBase {

	/**
	 * ID 生成器映射表. key: 生成器类型标识, value: 对应的 IdGenerator 实例
	 */
	private final Map<IdType, IdGenerator> generatorMap = new ConcurrentHashMap<>();

	private final SpringSnowflakeProperties springSnowflakeProperties;

	/**
	 * ID 类型不可用错误码.
	 */
	private static final String ID_TYPE_UNAVAILABLE = "ID_TYPE_UNAVAILABLE";

	public DistributedIdServiceImpl(SpringSnowflakeProperties springSnowflakeProperties,
			@Qualifier("snowflakeIdGenerator") IdGenerator snowflakeIdGenerator,
			@Qualifier("redisSegmentIdGenerator") IdGenerator redisSegmentIdGenerator) {
		this.springSnowflakeProperties = springSnowflakeProperties;
		if (snowflakeIdGenerator != null) {
			generatorMap.put(IdType.SNOWFLAKE, snowflakeIdGenerator);
			log.info("Registered IdGenerator for type: SNOWFLAKE");
		}
		if (redisSegmentIdGenerator != null) {
			generatorMap.put(IdType.REDIS_SEGMENT, redisSegmentIdGenerator);
			log.info("Registered IdGenerator for type: REDIS_SEGMENT");
		}
	}

	@Override
	public void generateId(GenerateIdRequest request, StreamObserver<GenerateIdResponse> responseObserver) {
		IdType type = request.getType();
		IdGenerator generator = getAvailableGenerator(type);

		if (generator == null) {
			GenerateIdResponse response = GenerateIdResponse.newBuilder()
				.setCode(ID_TYPE_UNAVAILABLE)
				.setMsg(String.format("ID generator for type [%s] is not available", type))
				.build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			return;
		}

		GenerateIdResponse response = GenerateIdResponse.newBuilder()
			.setCode(StatusCode.OK)
			.setMsg(MessageUtils.getMessage(StatusCode.OK))
			.setData(generator.nextId())
			.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void generateBatchIds(GenerateBatchIdRequest request,
			StreamObserver<GenerateBatchIdsResponse> responseObserver) {
		IdType type = request.getType();
		IdGenerator generator = getAvailableGenerator(type);

		if (generator == null) {
			GenerateBatchIdsResponse response = GenerateBatchIdsResponse.newBuilder()
				.setCode(ID_TYPE_UNAVAILABLE)
				.setMsg(String.format("ID generator for type [%s] is not available", type))
				.build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			return;
		}

		int num = Math.min(springSnowflakeProperties.getMaxBatchSize(), request.getNum());
		GenerateBatchIdsResponse.Builder builder = GenerateBatchIdsResponse.newBuilder()
			.setCode(StatusCode.OK)
			.setMsg(MessageUtils.getMessage(StatusCode.OK));
		List<Long> ids = generator.nextIds(num);
		for (Long id : ids) {
			builder.addData(id);
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}

	/**
	 * 获取可用的 ID 生成器.
	 * @param type ID 类型
	 * @return 可用的生成器，不存在或不可用时返回 null
	 */
	private IdGenerator getAvailableGenerator(IdType type) {
		IdGenerator generator = generatorMap.get(type);
		if (generator == null) {
			log.warn("No IdGenerator registered for type: {}", type);
			return null;
		}
		return generator;
	}

}
