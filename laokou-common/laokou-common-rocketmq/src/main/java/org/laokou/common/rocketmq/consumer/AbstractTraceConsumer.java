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

package org.laokou.common.rocketmq.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.laokou.common.core.utils.MDCUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.messaging.Message;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
public abstract class AbstractTraceConsumer {

	protected void putTrace(MessageExt messageExt) {
		String traceId = messageExt.getProperty(TRACE_ID);
		String spanId = messageExt.getProperty(SPAN_ID);
		MDCUtil.put(traceId, spanId);
	}

	protected void putTrace(Message message) {
		Object obj1 = message.getHeaders().get(TRACE_ID);
		Object obj2 = message.getHeaders().get(SPAN_ID);
		String traceId = ObjectUtil.isNull(obj1) ? EMPTY : obj1.toString();
		String spanId = ObjectUtil.isNull(obj2) ? EMPTY : obj2.toString();
		MDCUtil.put(traceId, spanId);
	}

	protected void clearTrace() {
		MDCUtil.clear();
	}

}
