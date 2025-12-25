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

package org.laokou.common.grpc;

import io.grpc.stub.StreamObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laokou.common.grpc.proto.HelloWorldProto;
import org.laokou.common.grpc.proto.SimpleGrpc;
import org.springframework.grpc.server.service.GrpcService;

/**
 * @author laokou
 */
@GrpcService
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {

	private static final Log log = LogFactory.getLog(GrpcServerService.class);

	@Override
	public void sayHello(HelloWorldProto.HelloRequest req,
			StreamObserver<HelloWorldProto.HelloReply> responseObserver) {
		log.info("Hello " + req.getName());
		if (req.getName().startsWith("error")) {
			throw new IllegalArgumentException("Bad name: " + req.getName());
		}
		if (req.getName().startsWith("internal")) {
			throw new RuntimeException();
		}
		HelloWorldProto.HelloReply reply = HelloWorldProto.HelloReply.newBuilder()
			.setMessage("Hello ==> " + req.getName())
			.build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

}
