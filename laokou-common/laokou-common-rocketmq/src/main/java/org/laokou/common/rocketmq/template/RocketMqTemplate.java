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

package org.laokou.common.rocketmq.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.laokou.common.core.utils.MDCUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.apache.rocketmq.client.producer.SendStatus.SEND_OK;
import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class RocketMqTemplate {

	private final RocketMQTemplate rocketMQTemplate;

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param tag 标签
	 * @param payload 消息
	 * @param traceId 链路ID
	 * @param <T> 泛型
	 */
	public <T> void sendAsyncMessage(String topic, String tag, T payload, String traceId, String spanId) {
		sendAsyncMessage(topic, tag, payload, 3000, traceId, spanId);
	}

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param tag 标签
	 * @param payload 消息
	 * @param traceId 链路ID
	 * @param <T> 泛型
	 */
	public <T> void sendAsyncMessage(String topic, String tag, T payload, long timeout, String traceId, String spanId) {
		Message<T> message = MessageBuilder.withPayload(payload)
			.setHeader(SPAN_ID, spanId)
			.setHeader(TRACE_ID, traceId)
			.build();
		sendAsyncMessage(topic, tag, message, timeout, traceId, spanId);
	}

	// @formatter:off
	/**
	 * 事务消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param transactionId 事务ID
	 * @param traceId 链路ID
	 * @param spanId 标签ID
	 * @param tag 标签
	 * @param <T> 泛型
	 */
	public <T> void sendTransactionMessage(String topic, String tag, T payload, Long transactionId, String traceId,
			String spanId) {
		try {
			Message<T> message = MessageBuilder.withPayload(payload)
				.setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
				.setHeader(TRACE_ID, traceId)
				.setHeader(SPAN_ID, spanId)
				.build();
			SendStatus sendStatus = rocketMQTemplate.sendMessageInTransaction(getTopicTag(topic, tag), message, null).getSendStatus();
			// 链路
			MDCUtil.put(traceId, spanId);
			if (ObjectUtil.equals(sendStatus, SEND_OK)) {
				log.info("RocketMQ事务消息发送成功【Tag标签】");
			}
			else {
				log.info("RocketMQ事务消息发送失败【Tag标签】，发送状态：{}", sendStatus.name());
			}
		}
		catch (Exception e) {
			log.error("RocketMQ事务消息发送失败【Tag标签】，报错信息：{}", e.getMessage(), e);
		}
		finally {
			MDCUtil.clear();
		}
	}

	private <T> void sendAsyncMessage(String topic, String tag, Message<T> message, long timeout, String traceId,
			String spanId) {
		// 链路
		MDCUtil.put(traceId, spanId);
		rocketMQTemplate.asyncSend(getTopicTag(topic, tag), message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("RocketMQ异步消息发送成功【Tag标签，指定超时时间】");
				MDCUtil.clear();
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("RocketMQ异步消息失败【Tag标签，指定超时时间】，报错信息：{}", throwable.getMessage(), throwable);
				MDCUtil.clear();
			}
		}, timeout);
	}
	// @formatter:on

	private String getTopicTag(String topic, String tag) {
		return StringUtil.isEmpty(tag) ? topic : String.format("%s:%s", topic, tag);
	}

}
