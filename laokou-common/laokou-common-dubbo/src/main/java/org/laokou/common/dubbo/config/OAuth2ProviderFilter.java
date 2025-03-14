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

package org.laokou.common.dubbo.config;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.rpc.*;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.security.config.GlobalOpaqueTokenIntrospector;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class OAuth2ProviderFilter implements Filter {

	private final GlobalOpaqueTokenIntrospector globalOpaqueTokenIntrospector;

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		String token = RpcContext.getServerAttachment().getAttachment(AUTHORIZATION);
		try {
			globalOpaqueTokenIntrospector.introspect(token);
			return invoker.invoke(invocation);
		}
		catch (OAuth2AuthenticationException ex) {
			OAuth2Error error = ex.getError();
			throw new BizException(error.getErrorCode(), error.getDescription());
		}
	}

}
