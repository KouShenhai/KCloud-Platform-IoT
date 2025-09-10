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

package org.laokou.common.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.apache.dubbo.rpc.RpcException;
import org.laokou.common.trace.util.MDCUtils;
import java.util.function.Supplier;

/**
 * @author laokou
 */
@Activate(group = CommonConstants.PROVIDER)
public class DubboTraceProviderFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		return invoke(RpcContext.getServerAttachment(), () -> invoker.invoke(invocation));
	}

	private Result invoke(RpcContextAttachment rpcContextAttachment, Supplier<Result> supplier) {
		String traceId = rpcContextAttachment.getAttachment(MDCUtils.TRACE_ID);
		String spanId = rpcContextAttachment.getAttachment(MDCUtils.SPAN_ID);
		if (StringUtils.isNotBlank(traceId) && StringUtils.isNotBlank(spanId)) {
			try {
				MDCUtils.put(traceId, spanId);
				return supplier.get();
			} finally {
				MDCUtils.clear();
			}
		} else {
			return supplier.get();
		}
	}

}
