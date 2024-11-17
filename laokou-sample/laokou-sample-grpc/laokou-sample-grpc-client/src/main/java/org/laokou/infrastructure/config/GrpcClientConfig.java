/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.infrastructure.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.laokou.common.grpc.config.SpringGrpcClientProperties;
import org.laokou.common.trace.utils.TraceUtil;
import org.laokou.grpc.user.UserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Configuration
public class GrpcClientConfig {

	@Bean
	public ManagedChannel managedChannel(SpringGrpcClientProperties springGrpcClientProperties, TraceUtil traceUtil) {
		return ManagedChannelBuilder
			.forAddress(springGrpcClientProperties.getIp(), springGrpcClientProperties.getPort())
			.usePlaintext()
			.idleTimeout(10, TimeUnit.MINUTES)
			.intercept(new GrpcClientInterceptor(traceUtil))
			.build();
	}

	@Bean
	public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(ManagedChannel managedChannel) {
		return UserServiceGrpc.newBlockingStub(managedChannel);
	}

}
