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

package org.laokou.common.grpc.server.config;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.MessageUtils;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
@GlobalServerInterceptor
final class OAuth2ServerInterceptor implements ServerInterceptor {

	private final Metadata.Key<String> authorization = Metadata.Key.of("Authorization",
			Metadata.ASCII_STRING_MARSHALLER);

	private final Metadata.Key<String> serviceId = Metadata.Key.of("Service-Id", Metadata.ASCII_STRING_MARSHALLER);

	private final JwtDecoder jwtDecoder;

	private final SystemSettingsProperties systemSettingsProperties;

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata,
			ServerCallHandler<ReqT, RespT> serverCallHandler) {
		try {
			return serverCallHandler.startCall(serverCall, metadata);
		}
		catch (Exception e) {
			serverCall.close(Status.UNAUTHENTICATED.withDescription(MessageUtils.getMessage(StatusCode.UNAUTHORIZED)),
					new Metadata());
			return new ServerCall.Listener<>() {
			};
		}
	}

}
