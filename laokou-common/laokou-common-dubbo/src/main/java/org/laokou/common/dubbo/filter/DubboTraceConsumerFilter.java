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
import org.apache.dubbo.rpc.*;
import org.laokou.common.core.util.SpringContextUtils;
import org.laokou.common.i18n.common.constant.TraceConstants;
import org.laokou.common.trace.util.TraceUtils;

/**
 * @author laokou
 */
@Activate(group = CommonConstants.CONSUMER)
public class DubboTraceConsumerFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		TraceUtils traceUtils = SpringContextUtils.getBean(TraceUtils.class);
		RpcContextAttachment clientAttachment = RpcContext.getClientAttachment();
		clientAttachment.setAttachment(TraceConstants.TRACE_ID, traceUtils.getTraceId());
		clientAttachment.setAttachment(TraceConstants.SPAN_ID, traceUtils.getSpanId());
		return invoker.invoke(invocation);
	}

}
