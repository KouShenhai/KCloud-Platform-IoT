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

package org.laokou.common.domain.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.MDCUtil;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.laokou.common.i18n.utils.StringUtil;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDomainEventConsumer implements RocketMQListener<MessageExt> {

	protected final DomainEventPublisher rocketMQDomainEventPublisher;

	@Override
	public void onMessage(MessageExt messageExt) {
		try {
			putTrace(messageExt);
			String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
			handleDomainEvent(convert(msg));
		}
		catch (Exception e) {
			log.error("消费失败，主题Topic：{}，偏移量Offset：{}，错误信息：{}", messageExt.getTopic(), messageExt.getCommitLogOffset(),
				StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(), e);
			throw e;
		}
		finally {
			clearTrace();
		}
	}

	protected abstract void handleDomainEvent(DefaultDomainEvent domainEvent);

	protected abstract DefaultDomainEvent convert(String msg);

	private void putTrace(MessageExt messageExt) {
		String traceId = messageExt.getProperty(TRACE_ID);
		String spanId = messageExt.getProperty(SPAN_ID);
		MDCUtil.put(traceId, spanId);
	}

	private void clearTrace() {
		MDCUtil.clear();
	}

}
