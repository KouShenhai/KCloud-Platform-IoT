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

package org.laokou.common.rocketmq.handler;

import org.apache.rocketmq.common.message.MessageExt;
import org.laokou.common.core.utils.MDCUtil;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
public class TraceHandler {

	protected void putTrace(MessageExt messageExt) {
		String traceId = messageExt.getProperty(TRACE_ID);
		String spanId = messageExt.getProperty(SPAN_ID);
		MDCUtil.put(traceId, spanId);
	}

	protected void putTrace(Message message) {
		Object obj1 = message.getHeaders().get(TRACE_ID);
		Object obj2 = message.getHeaders().get(SPAN_ID);
		Assert.notNull(obj1, "链路ID不为空");
		Assert.notNull(obj2, "标签ID不为空");
		MDCUtil.put(obj1.toString(), obj2.toString());
	}

	protected void clearTrace() {
		MDCUtil.clear();
	}

}
