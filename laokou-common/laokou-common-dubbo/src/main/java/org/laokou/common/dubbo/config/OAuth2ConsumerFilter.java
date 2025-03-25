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

import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.rpc.*;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.StringUtils;

import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class OAuth2ConsumerFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String token = request.getHeader(AUTHORIZATION);
		if (StringUtils.isEmpty(token)) {
			throw new BizException(UNAUTHORIZED);
		}
		RpcContext.getClientAttachment().setAttachment(AUTHORIZATION, token);
		return invoker.invoke(invocation);
	}

}
