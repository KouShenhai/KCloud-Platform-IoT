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
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.MDCUtil;
import org.laokou.domain.model.TraceLogV;

import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
public class GrpcServerInterceptor implements ServerInterceptor {

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata,
			ServerCallHandler<ReqT, RespT> serverCallHandler) {
		try {
			String traceId = metadata.get(Metadata.Key.of(TRACE_ID, Metadata.ASCII_STRING_MARSHALLER));
			String spanId = metadata.get(Metadata.Key.of(SPAN_ID, Metadata.ASCII_STRING_MARSHALLER));
			MDCUtil.put(traceId, spanId);
			return new WrapperTrace<>(serverCall, serverCallHandler, metadata, new TraceLogV(traceId, spanId));
		}
		finally {
			MDCUtil.clear();
		}
	}

	private static class WrapperTrace<R, S> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<R> {

		private final TraceLogV traceLog;

		protected WrapperTrace(ServerCall<R, S> serverCall, ServerCallHandler<R, S> serverCallHandler,
				Metadata metadata, TraceLogV traceLog) {
			super(serverCallHandler.startCall(new WrapperListener<>(serverCall), metadata));
			this.traceLog = traceLog;
		}

		@Override
		public void onMessage(R message) {
			try {
				MDCUtil.put(traceLog.traceId(), traceLog.spanId());
				log.info("onMessage...");
				super.onMessage(message);
			}
			finally {
				MDCUtil.clear();
			}
		}

		@Override
		public void onHalfClose() {
			try {
				MDCUtil.put(traceLog.traceId(), traceLog.spanId());
				log.info("onHalfClose...");
				super.onHalfClose();
			}
			finally {
				MDCUtil.clear();
			}
		}

		@Override
		public void onComplete() {
			try {
				MDCUtil.put(traceLog.traceId(), traceLog.spanId());
				log.info("onComplete...");
				super.onComplete();
			}
			finally {
				MDCUtil.clear();
			}
		}

	}

	private static class WrapperListener<R, S> extends ForwardingServerCall.SimpleForwardingServerCall<R, S> {

		protected WrapperListener(ServerCall<R, S> serverCall) {
			super(serverCall);
		}

		@Override
		public void sendMessage(S message) {
			super.sendMessage(message);
		}

	}

}
