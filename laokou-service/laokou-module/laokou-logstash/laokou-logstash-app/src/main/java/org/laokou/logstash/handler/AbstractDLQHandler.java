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

package org.laokou.logstash.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.common.core.utils.MDCUtil;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * 监听死信队列.
 *
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDLQHandler implements RocketMQListener<MessageExt> {

	private final RocketMqTemplate rocketMqTemplate;

	@Override
	public void onMessage(MessageExt messageExt) {
		String traceId = messageExt.getProperty(TRACE_ID);
		String spanId = messageExt.getProperty(SPAN_ID);
		MDCUtil.put(traceId, spanId);
		String topic = messageExt.getTopic();
		String tag = messageExt.getTags();
		Object payload = new String(messageExt.getBody(), StandardCharsets.UTF_8);
		log.info("接收消息 ===> topic：{}，tag：{}，msg：{}", topic, tag, payload);
		try {
			if (StringUtil.isNotEmpty(tag)) {
				rocketMqTemplate.sendAsyncMessage(topic, tag, payload, traceId, spanId);
			}
			else {
				rocketMqTemplate.sendAsyncMessage(topic, EMPTY, payload, traceId, spanId);
			}
		}
		finally {
			MDCUtil.clear();
		}
	}

}
