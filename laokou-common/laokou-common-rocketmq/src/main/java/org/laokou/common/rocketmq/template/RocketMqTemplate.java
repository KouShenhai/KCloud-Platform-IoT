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
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.apache.rocketmq.client.producer.SendStatus.SEND_OK;
import static org.laokou.common.i18n.common.constant.StringConstant.NULL;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
public class RocketMqTemplate {

	private static final String TOPIC_TAG = "%s:%s";

	private final RocketMQTemplate rocketMQTemplate;

	/**
	 * 同步发送.
	 * @param topic 主题
	 * @param payload 消息
	 * @param timeout 超时时间
	 * @param <T> 泛型
	 * @return 发送结果
	 */
	public <T> boolean sendSyncMessage(String topic, T payload, long timeout) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSend(topic, message, timeout).getSendStatus().equals(SEND_OK);
	}

	/**
	 * 同步发送.
	 * @param topic 主题
	 * @param payload 消息
	 * @param timeout 超时时间
	 * @param <T> 泛型
	 * @param delayLevel 延迟等级
	 * @return 发送结果
	 */
	public <T> boolean sendSyncMessage(String topic, T payload, long timeout, int delayLevel) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSend(topic, message, timeout, delayLevel).getSendStatus().equals(SEND_OK);
	}

	/**
	 * 同步发送消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 */
	public <T> boolean sendSyncMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSend(topic, message).getSendStatus().equals(SEND_OK);
	}

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 */
	public <T> void sendAsyncMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		sendAsyncMessage(topic, message);
	}

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param tag 标签
	 * @param payload 消息
	 * @param <T> 泛型
	 */
	public <T> void sendAsyncMessage(String topic, String tag, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		sendAsyncMessage(topic, tag, message);
	}

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param tag 标签
	 * @param payload 消息
	 * @param traceId 链路ID
	 * @param <T> 泛型
	 */
	public <T> void sendAsyncMessage(String topic, String tag, T payload, String traceId) {
		Message<T> message = MessageBuilder.withPayload(payload).setHeader(TRACE_ID, traceId).build();
		sendAsyncMessage(topic, tag, message);
	}

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param tag 标签
	 * @param payload 消息
	 * @param traceId 链路ID
	 * @param timeout 超时时间
	 * @param <T> 泛型
	 */
	public <T> void sendAsyncMessage(String topic, String tag, T payload, String traceId, long timeout) {
		Message<T> message = MessageBuilder.withPayload(payload).setHeader(TRACE_ID, traceId).build();
		sendAsyncMessage(topic, tag, message, timeout);
	}

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param traceId 链路ID
	 * @param <T> 泛型
	 */
	public <T> void sendAsyncMessage(String topic, T payload, String traceId) {
		Message<T> message = MessageBuilder.withPayload(payload).setHeader(TRACE_ID, traceId).build();
		sendAsyncMessage(topic, message);
	}

	/**
	 * 异步发送消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @param timeout 超时时间
	 */
	public <T> void sendAsyncMessage(String topic, T payload, long timeout) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		sendAsyncMessage(topic, message, timeout);
	}

	/**
	 * 单向发送消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 */
	public <T> void sendOneWayMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		// 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
		// 适用于耗时短，但对可靠性不高的场景，如日志收集
		rocketMQTemplate.sendOneWay(topic, message);
	}

	/**
	 * 延迟消息.
	 * @param topic 主题
	 * @param delay 延迟时间
	 * @param payload 消息
	 * @param <T> 泛型
	 */
	public <T> boolean sendDelayMessage(String topic, long delay, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSendDelayTimeSeconds(topic, payload, delay).getSendStatus().equals(SEND_OK);
	}

	/**
	 * 同步发送顺序消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @param id 标识
	 */
	public <T> boolean sendSyncOrderlyMessage(String topic, T payload, String id) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSendOrderly(topic, message, id).getSendStatus().equals(SEND_OK);
	}

	/**
	 * 异步发送顺序消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @param id 标识
	 */
	public <T> void sendAsyncOrderlyMessage(String topic, T payload, String id) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		rocketMQTemplate.asyncSendOrderly(topic, message, id, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("RocketMQ异步顺序消息发送成功【无Tag标签】");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("RocketMQ异步顺序消息发送失败【无Tag标签】，报错信息：{}，详情见日志", throwable.getMessage(), throwable);
			}
		});
	}

	/**
	 * 单向发送顺序消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @param id 标识
	 */
	public <T> void sendOneWayOrderlyMessage(String topic, T payload, String id) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		// 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
		// 适用于耗时短，但对可靠性不高的场景，如日志收集
		rocketMQTemplate.sendOneWayOrderly(topic, message, id);
	}

	/**
	 * 事务消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param transactionId 事务ID
	 * @param <T> 泛型
	 * @return 发送结果
	 */
	public <T> boolean sendTransactionMessage(String topic, T payload, Long transactionId) {
		Message<T> message = MessageBuilder.withPayload(payload)
			.setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
			.build();
		return rocketMQTemplate.sendMessageInTransaction(topic, message, NULL).getSendStatus().equals(SEND_OK);
	}

	/**
	 * 转换并发送.
	 * @param topic 主题
	 * @param payload 消息内容
	 * @param <T> 泛型
	 */
	public <T> void convertAndSendMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		rocketMQTemplate.convertAndSend(topic, message);
	}

	/**
	 * 发送并接收.
	 * @param topic 主题
	 * @param payload 内容
	 * @param clazz 类型
	 * @param <T> 泛型
	 */
	public <T> Object sendAndReceiveMessage(String topic, T payload, Class<?> clazz) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.sendAndReceive(topic, message, clazz);
	}

	private <T> void sendAsyncMessage(String topic, String tag, Message<T> message) {
		rocketMQTemplate.asyncSend(String.format(TOPIC_TAG, topic, tag), message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("RocketMQ异步消息发送成功【Tag标签，默认超时时间】");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("RocketMQ异步消息失败【Tag标签，默认超时时间】，报错信息", throwable);
			}
		});
	}

	private <T> void sendAsyncMessage(String topic, String tag, Message<T> message, long timeout) {
		rocketMQTemplate.asyncSend(String.format(TOPIC_TAG, topic, tag), message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("RocketMQ异步消息发送成功【Tag标签，指定超时时间】");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("RocketMQ异步消息失败【Tag标签，指定超时时间】，报错信息", throwable);
			}
		}, timeout);
	}

	private <T> void sendAsyncMessage(String topic, Message<T> message, long timeout) {
		rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("RocketMQ异步消息发送成功【无Tag标签，指定超时时间】");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("RocketMQ异步消息发送失败【无Tag标签，指定超时时间】，报错信息", throwable);
			}
		}, timeout);
	}

	private <T> void sendAsyncMessage(String topic, Message<T> message) {
		rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("RocketMQ异步消息发送成功【无Tag标签，默认超时时间】");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("RocketMQ异步消息发送失败【无Tag标签，默认超时时间】，报错信息", throwable);
			}
		});
	}

}
