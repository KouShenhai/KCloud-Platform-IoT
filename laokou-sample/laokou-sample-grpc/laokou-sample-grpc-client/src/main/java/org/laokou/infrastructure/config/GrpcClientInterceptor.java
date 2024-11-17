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

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import org.laokou.common.trace.utils.TraceUtil;
import org.laokou.domain.model.TraceLogV;

import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public class GrpcClientInterceptor implements ClientInterceptor {

	private final TraceUtil traceUtil;

	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
			CallOptions callOptions, Channel channel) {
		String traceId = traceUtil.getTraceId();
		String spanId = traceUtil.getSpanId();
		return new WrapperTrace<>(methodDescriptor, callOptions, channel, new TraceLogV(traceId, spanId));
	}

	private static class WrapperTrace<R, S> extends ForwardingClientCall.SimpleForwardingClientCall<R, S> {

		protected WrapperTrace(MethodDescriptor<R, S> methodDescriptor, CallOptions callOptions, Channel channel,
				TraceLogV traceLog) {
			super(new SimpleForwardingClientCall<>(channel.newCall(methodDescriptor, callOptions)) {

				@Override
				public void sendMessage(R message) {
					super.sendMessage(message);
				}

				@Override
				public void start(Listener<S> responseListener, Metadata metadata) {
					metadata.put(Metadata.Key.of(TRACE_ID, Metadata.ASCII_STRING_MARSHALLER), traceLog.traceId());
					metadata.put(Metadata.Key.of(SPAN_ID, Metadata.ASCII_STRING_MARSHALLER), traceLog.spanId());
					super.start(new WrapperStart<>(responseListener), metadata);
				}
			});
		}

	}

	private static class WrapperStart<S> extends ForwardingClientCallListener.SimpleForwardingClientCallListener<S> {

		protected WrapperStart(ClientCall.Listener<S> listener) {
			super(listener);
		}

	}

}
