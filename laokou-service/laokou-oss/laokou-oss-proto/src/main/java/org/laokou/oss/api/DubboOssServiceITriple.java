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

package org.laokou.oss.api;

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

public final class DubboOssServiceITriple {

	public static final String SERVICE_NAME = OssServiceI.SERVICE_NAME;

	private static final StubServiceDescriptor serviceDescriptor = new StubServiceDescriptor(SERVICE_NAME,
			OssServiceI.class);

	static {
		org.apache.dubbo.rpc.protocol.tri.service.SchemaDescriptorRegistry.addSchemaDescriptor(SERVICE_NAME,
				Oss.getDescriptor());
		StubSuppliers.addSupplier(SERVICE_NAME, DubboOssServiceITriple::newStub);
		StubSuppliers.addSupplier(OssServiceI.JAVA_SERVICE_NAME, DubboOssServiceITriple::newStub);
		StubSuppliers.addDescriptor(SERVICE_NAME, serviceDescriptor);
		StubSuppliers.addDescriptor(OssServiceI.JAVA_SERVICE_NAME, serviceDescriptor);
	}

	@SuppressWarnings("unchecked")
	public static OssServiceI newStub(Invoker<?> invoker) {
		return new OssServiceIStub((Invoker<OssServiceI>) invoker);
	}

	private static final StubMethodDescriptor uploadOssMethod = new StubMethodDescriptor("uploadOss",
			org.laokou.oss.api.OssUploadCmd.class, org.laokou.oss.api.OssUploadResult.class,
			MethodDescriptor.RpcType.UNARY, obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.oss.api.OssUploadCmd::parseFrom, org.laokou.oss.api.OssUploadResult::parseFrom);

	private static final StubMethodDescriptor uploadOssAsyncMethod = new StubMethodDescriptor("uploadOss",
			org.laokou.oss.api.OssUploadCmd.class, java.util.concurrent.CompletableFuture.class,
			MethodDescriptor.RpcType.UNARY, obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.oss.api.OssUploadCmd::parseFrom, org.laokou.oss.api.OssUploadResult::parseFrom);

	private static final StubMethodDescriptor uploadOssProxyAsyncMethod = new StubMethodDescriptor("uploadOssAsync",
			org.laokou.oss.api.OssUploadCmd.class, org.laokou.oss.api.OssUploadResult.class,
			MethodDescriptor.RpcType.UNARY, obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(),
			org.laokou.oss.api.OssUploadCmd::parseFrom, org.laokou.oss.api.OssUploadResult::parseFrom);

	static {
		serviceDescriptor.addMethod(uploadOssMethod);
		serviceDescriptor.addMethod(uploadOssProxyAsyncMethod);
	}

	public static class OssServiceIStub implements OssServiceI, Destroyable {

		private final Invoker<OssServiceI> invoker;

		public OssServiceIStub(Invoker<OssServiceI> invoker) {
			this.invoker = invoker;
		}

		@Override
		public void $destroy() {
			invoker.destroy();
		}

		@Override
		public org.laokou.oss.api.OssUploadResult uploadOss(org.laokou.oss.api.OssUploadCmd request) {
			return StubInvocationUtil.unaryCall(invoker, uploadOssMethod, request);
		}

		public CompletableFuture<org.laokou.oss.api.OssUploadResult> uploadOssAsync(
				org.laokou.oss.api.OssUploadCmd request) {
			return StubInvocationUtil.unaryCall(invoker, uploadOssAsyncMethod, request);
		}

		public void uploadOss(org.laokou.oss.api.OssUploadCmd request,
				StreamObserver<org.laokou.oss.api.OssUploadResult> responseObserver) {
			StubInvocationUtil.unaryCall(invoker, uploadOssMethod, request, responseObserver);
		}

	}

	public static abstract class OssServiceIImplBase implements OssServiceI, ServerService<OssServiceI> {

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
		public CompletableFuture<org.laokou.oss.api.OssUploadResult> uploadOssAsync(
				org.laokou.oss.api.OssUploadCmd request) {
			return CompletableFuture.completedFuture(uploadOss(request));
		}

		// This server stream type unary method is <b>only</b> used for generated stub to
		// support async unary method.
		// It will not be called if you are NOT using Dubbo3 generated triple stub and
		// <b>DO NOT</b> implement this method.

		public void uploadOss(org.laokou.oss.api.OssUploadCmd request,
				StreamObserver<org.laokou.oss.api.OssUploadResult> responseObserver) {
			uploadOssAsync(request).whenComplete((r, t) -> {
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
		public final Invoker<OssServiceI> getInvoker(URL url) {
			PathResolver pathResolver = url.getOrDefaultFrameworkModel()
				.getExtensionLoader(PathResolver.class)
				.getDefaultExtension();
			Map<String, StubMethodHandler<?, ?>> handlers = new HashMap<>();
			pathResolver.addNativeStub("/" + SERVICE_NAME + "/uploadOss");
			pathResolver.addNativeStub("/" + SERVICE_NAME + "/uploadOssAsync");
			// for compatibility
			pathResolver.addNativeStub("/" + JAVA_SERVICE_NAME + "/uploadOss");
			pathResolver.addNativeStub("/" + JAVA_SERVICE_NAME + "/uploadOssAsync");
			BiConsumer<org.laokou.oss.api.OssUploadCmd, StreamObserver<org.laokou.oss.api.OssUploadResult>> uploadOssFunc = this::uploadOss;
			handlers.put(uploadOssMethod.getMethodName(), new UnaryStubMethodHandler<>(uploadOssFunc));
			BiConsumer<org.laokou.oss.api.OssUploadCmd, StreamObserver<org.laokou.oss.api.OssUploadResult>> uploadOssAsyncFunc = syncToAsync(
					this::uploadOss);
			handlers.put(uploadOssProxyAsyncMethod.getMethodName(), new UnaryStubMethodHandler<>(uploadOssAsyncFunc));

			return new StubInvoker<>(this, url, OssServiceI.class, handlers);
		}

		@Override
		public org.laokou.oss.api.OssUploadResult uploadOss(org.laokou.oss.api.OssUploadCmd request) {
			throw unimplementedMethodException(uploadOssMethod);
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
