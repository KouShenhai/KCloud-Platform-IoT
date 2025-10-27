/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.distributed.identifier.api;

import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.PathResolver;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.ServerService;
import org.apache.dubbo.rpc.TriRpcStatus;
import org.apache.dubbo.rpc.model.MethodDescriptor;
import org.apache.dubbo.rpc.model.ServiceDescriptor;
import org.apache.dubbo.rpc.model.StubMethodDescriptor;
import org.apache.dubbo.rpc.model.StubServiceDescriptor;
import org.apache.dubbo.rpc.service.Destroyable;
import org.apache.dubbo.rpc.stub.BiStreamMethodHandler;
import org.apache.dubbo.rpc.stub.ServerStreamMethodHandler;
import org.apache.dubbo.rpc.stub.StubInvocationUtil;
import org.apache.dubbo.rpc.stub.StubInvoker;
import org.apache.dubbo.rpc.stub.StubMethodHandler;
import org.apache.dubbo.rpc.stub.StubSuppliers;
import org.apache.dubbo.rpc.stub.UnaryStubMethodHandler;

import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.concurrent.CompletableFuture;

public final class DubboDistributedIdentifierServiceITriple {

	public static final String SERVICE_NAME = DistributedIdentifierServiceI.SERVICE_NAME;

	private static final StubServiceDescriptor serviceDescriptor = new StubServiceDescriptor(SERVICE_NAME,
			DistributedIdentifierServiceI.class);

	static {
		org.apache.dubbo.rpc.protocol.tri.service.SchemaDescriptorRegistry.addSchemaDescriptor(SERVICE_NAME,
				Oss.getDescriptor());
		StubSuppliers.addSupplier(SERVICE_NAME, DubboDistributedIdentifierServiceITriple::newStub);
		StubSuppliers.addSupplier(DistributedIdentifierServiceI.JAVA_SERVICE_NAME,
				DubboDistributedIdentifierServiceITriple::newStub);
		StubSuppliers.addDescriptor(SERVICE_NAME, serviceDescriptor);
		StubSuppliers.addDescriptor(DistributedIdentifierServiceI.JAVA_SERVICE_NAME, serviceDescriptor);
	}

	@SuppressWarnings("unchecked")
	public static DistributedIdentifierServiceI newStub(Invoker<?> invoker) {
		return new DistributedIdentifierServiceIStub((Invoker<DistributedIdentifierServiceI>) invoker);
	}

	private static final StubMethodDescriptor generateSnowflakeMethod = new StubMethodDescriptor("generateSnowflake",
			org.laokou.distributed.identifier.api.DistributedIdentifierCmd.class,
			org.laokou.distributed.identifier.api.DistributedIdentifierResult.class, MethodDescriptor.RpcType.UNARY,
			obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.distributed.identifier.api.DistributedIdentifierCmd::parseFrom,
			org.laokou.distributed.identifier.api.DistributedIdentifierResult::parseFrom);

	private static final StubMethodDescriptor generateSnowflakeAsyncMethod = new StubMethodDescriptor(
			"generateSnowflake", org.laokou.distributed.identifier.api.DistributedIdentifierCmd.class,
			java.util.concurrent.CompletableFuture.class, MethodDescriptor.RpcType.UNARY,
			obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.distributed.identifier.api.DistributedIdentifierCmd::parseFrom,
			org.laokou.distributed.identifier.api.DistributedIdentifierResult::parseFrom);

	private static final StubMethodDescriptor generateSnowflakeProxyAsyncMethod = new StubMethodDescriptor(
			"generateSnowflakeAsync", org.laokou.distributed.identifier.api.DistributedIdentifierCmd.class,
			org.laokou.distributed.identifier.api.DistributedIdentifierResult.class, MethodDescriptor.RpcType.UNARY,
			obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.distributed.identifier.api.DistributedIdentifierCmd::parseFrom,
			org.laokou.distributed.identifier.api.DistributedIdentifierResult::parseFrom);

	private static final StubMethodDescriptor generateSnowflakeBatchMethod = new StubMethodDescriptor(
			"generateSnowflakeBatch", org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd.class,
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult.class,
			MethodDescriptor.RpcType.UNARY, obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd::parseFrom,
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult::parseFrom);

	private static final StubMethodDescriptor generateSnowflakeBatchAsyncMethod = new StubMethodDescriptor(
			"generateSnowflakeBatch", org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd.class,
			java.util.concurrent.CompletableFuture.class, MethodDescriptor.RpcType.UNARY,
			obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd::parseFrom,
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult::parseFrom);

	private static final StubMethodDescriptor generateSnowflakeBatchProxyAsyncMethod = new StubMethodDescriptor(
			"generateSnowflakeBatchAsync", org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd.class,
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult.class,
			MethodDescriptor.RpcType.UNARY, obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd::parseFrom,
			org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult::parseFrom);

	static {
		serviceDescriptor.addMethod(generateSnowflakeMethod);
		serviceDescriptor.addMethod(generateSnowflakeProxyAsyncMethod);
		serviceDescriptor.addMethod(generateSnowflakeBatchMethod);
		serviceDescriptor.addMethod(generateSnowflakeBatchProxyAsyncMethod);
	}

	public static class DistributedIdentifierServiceIStub implements DistributedIdentifierServiceI, Destroyable {

		private final Invoker<DistributedIdentifierServiceI> invoker;

		public DistributedIdentifierServiceIStub(Invoker<DistributedIdentifierServiceI> invoker) {
			this.invoker = invoker;
		}

		@Override
		public void $destroy() {
			invoker.destroy();
		}

		@Override
		public org.laokou.distributed.identifier.api.DistributedIdentifierResult generateSnowflake(
				org.laokou.distributed.identifier.api.DistributedIdentifierCmd request) {
			return StubInvocationUtil.unaryCall(invoker, generateSnowflakeMethod, request);
		}

		public CompletableFuture<org.laokou.distributed.identifier.api.DistributedIdentifierResult> generateSnowflakeAsync(
				org.laokou.distributed.identifier.api.DistributedIdentifierCmd request) {
			return StubInvocationUtil.unaryCall(invoker, generateSnowflakeAsyncMethod, request);
		}

		public void generateSnowflake(org.laokou.distributed.identifier.api.DistributedIdentifierCmd request,
				StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierResult> responseObserver) {
			StubInvocationUtil.unaryCall(invoker, generateSnowflakeMethod, request, responseObserver);
		}

		@Override
		public org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult generateSnowflakeBatch(
				org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request) {
			return StubInvocationUtil.unaryCall(invoker, generateSnowflakeBatchMethod, request);
		}

		public CompletableFuture<org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult> generateSnowflakeBatchAsync(
				org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request) {
			return StubInvocationUtil.unaryCall(invoker, generateSnowflakeBatchAsyncMethod, request);
		}

		public void generateSnowflakeBatch(org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request,
				StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult> responseObserver) {
			StubInvocationUtil.unaryCall(invoker, generateSnowflakeBatchMethod, request, responseObserver);
		}

	}

	public static abstract class DistributedIdentifierServiceIImplBase
			implements DistributedIdentifierServiceI, ServerService<DistributedIdentifierServiceI> {

		private <T, R> BiConsumer<T, StreamObserver<R>> syncToAsync(java.util.function.Function<T, R> syncFun) {
			return new BiConsumer<T, StreamObserver<R>>() {
				@Override
				public void accept(T t, StreamObserver<R> observer) {
					try {
						R ret = syncFun.apply(t);
						observer.onNext(ret);
						observer.onCompleted();
					}
					catch (Throwable e) {
						observer.onError(e);
					}
				}
			};
		}

		@Override
		public CompletableFuture<org.laokou.distributed.identifier.api.DistributedIdentifierResult> generateSnowflakeAsync(
				org.laokou.distributed.identifier.api.DistributedIdentifierCmd request) {
			return CompletableFuture.completedFuture(generateSnowflake(request));
		}

		@Override
		public CompletableFuture<org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult> generateSnowflakeBatchAsync(
				org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request) {
			return CompletableFuture.completedFuture(generateSnowflakeBatch(request));
		}

		// This server stream type unary method is <b>only</b> used for generated stub to
		// support async unary method.
		// It will not be called if you are NOT using Dubbo3 generated triple stub and
		// <b>DO NOT</b> implement this method.

		public void generateSnowflake(org.laokou.distributed.identifier.api.DistributedIdentifierCmd request,
				StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierResult> responseObserver) {
			generateSnowflakeAsync(request).whenComplete((r, t) -> {
				if (t != null) {
					responseObserver.onError(t);
				}
				else {
					responseObserver.onNext(r);
					responseObserver.onCompleted();
				}
			});
		}

		public void generateSnowflakeBatch(org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request,
				StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult> responseObserver) {
			generateSnowflakeBatchAsync(request).whenComplete((r, t) -> {
				if (t != null) {
					responseObserver.onError(t);
				}
				else {
					responseObserver.onNext(r);
					responseObserver.onCompleted();
				}
			});
		}

		@Override
		public final Invoker<DistributedIdentifierServiceI> getInvoker(URL url) {
			PathResolver pathResolver = url.getOrDefaultFrameworkModel()
				.getExtensionLoader(PathResolver.class)
				.getDefaultExtension();
			Map<String, StubMethodHandler<?, ?>> handlers = new HashMap<>();
			pathResolver.addNativeStub("/" + SERVICE_NAME + "/generateSnowflake");
			pathResolver.addNativeStub("/" + SERVICE_NAME + "/generateSnowflakeAsync");
			// for compatibility
			pathResolver.addNativeStub("/" + JAVA_SERVICE_NAME + "/generateSnowflake");
			pathResolver.addNativeStub("/" + JAVA_SERVICE_NAME + "/generateSnowflakeAsync");
			pathResolver.addNativeStub("/" + SERVICE_NAME + "/generateSnowflakeBatch");
			pathResolver.addNativeStub("/" + SERVICE_NAME + "/generateSnowflakeBatchAsync");
			// for compatibility
			pathResolver.addNativeStub("/" + JAVA_SERVICE_NAME + "/generateSnowflakeBatch");
			pathResolver.addNativeStub("/" + JAVA_SERVICE_NAME + "/generateSnowflakeBatchAsync");
			BiConsumer<org.laokou.distributed.identifier.api.DistributedIdentifierCmd, StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierResult>> generateSnowflakeFunc = this::generateSnowflake;
			handlers.put(generateSnowflakeMethod.getMethodName(), new UnaryStubMethodHandler<>(generateSnowflakeFunc));
			BiConsumer<org.laokou.distributed.identifier.api.DistributedIdentifierCmd, StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierResult>> generateSnowflakeAsyncFunc = syncToAsync(
					this::generateSnowflake);
			handlers.put(generateSnowflakeProxyAsyncMethod.getMethodName(),
					new UnaryStubMethodHandler<>(generateSnowflakeAsyncFunc));
			BiConsumer<org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd, StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult>> generateSnowflakeBatchFunc = this::generateSnowflakeBatch;
			handlers.put(generateSnowflakeBatchMethod.getMethodName(),
					new UnaryStubMethodHandler<>(generateSnowflakeBatchFunc));
			BiConsumer<org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd, StreamObserver<org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult>> generateSnowflakeBatchAsyncFunc = syncToAsync(
					this::generateSnowflakeBatch);
			handlers.put(generateSnowflakeBatchProxyAsyncMethod.getMethodName(),
					new UnaryStubMethodHandler<>(generateSnowflakeBatchAsyncFunc));

			return new StubInvoker<>(this, url, DistributedIdentifierServiceI.class, handlers);
		}

		@Override
		public org.laokou.distributed.identifier.api.DistributedIdentifierResult generateSnowflake(
				org.laokou.distributed.identifier.api.DistributedIdentifierCmd request) {
			throw unimplementedMethodException(generateSnowflakeMethod);
		}

		@Override
		public org.laokou.distributed.identifier.api.DistributedIdentifierBatchResult generateSnowflakeBatch(
				org.laokou.distributed.identifier.api.DistributedIdentifierBatchCmd request) {
			throw unimplementedMethodException(generateSnowflakeBatchMethod);
		}

		@Override
		public final ServiceDescriptor getServiceDescriptor() {
			return serviceDescriptor;
		}

		private RpcException unimplementedMethodException(StubMethodDescriptor methodDescriptor) {
			return TriRpcStatus.UNIMPLEMENTED
				.withDescription(String.format("Method %s is unimplemented",
						"/" + serviceDescriptor.getInterfaceName() + "/" + methodDescriptor.getMethodName()))
				.asException();
		}

	}

}
