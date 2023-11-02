/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.rocketmq.constant.MqConstant;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.Constant.TRACE_ID;

/**
 * @author laokou
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RocketMqTemplate implements InitializingBean {

	private final RocketMQTemplate rocketMQTemplate;

	private final ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 同步发送
	 * @param topic
	 * @param payload
	 * @param timeout
	 * @return
	 */
	public <T> boolean sendSyncMessage(String topic, T payload, long timeout) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSend(topic, message, timeout).getSendStatus().equals(SendStatus.SEND_OK);
	}

	/**
	 * 同步发送
	 * @param topic
	 * @param payload
	 * @param timeout
	 * @return
	 */
	public <T> boolean sendSyncMessage(String topic, T payload, long timeout, int delayLevel) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSend(topic, message, timeout, delayLevel)
			.getSendStatus()
			.equals(SendStatus.SEND_OK);
	}

	/**
	 * 同步发送消息
	 * @param topic topic
	 * @param payload payload
	 */
	public <T> boolean sendSyncMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSend(topic, message).getSendStatus().equals(SendStatus.SEND_OK);
	}

	/**
	 * 异步发送消息
	 * @param topic topic
	 * @param payload payload
	 */
	public <T> void sendAsyncMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("发送成功");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("报错信息", throwable);
			}
		});
	}

	/**
	 * 异步发送消息
	 * @param topic topic
	 * @param tag
	 * @param payload payload
	 */
	public <T> void sendAsyncMessage(String topic, String tag, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		sendAsyncMessage(topic, tag, message);
	}

	/**
	 * 异步发送消息
	 * @param topic topic
	 * @param tag
	 * @param payload payload
	 * @param traceId traceId
	 */
	public <T> void sendAsyncMessage(String topic, String tag, T payload, String traceId) {
		Message<T> message = MessageBuilder.withPayload(payload).setHeader(TRACE_ID, traceId).build();
		sendAsyncMessage(topic, tag, message);
	}

	/**
	 * 异步发送消息
	 * @param topic topic
	 * @param payload payload
	 */
	public <T> void sendAsyncMessage(String topic, T payload, long timeout) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("发送成功");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("报错信息：{}", throwable.getMessage());
			}
		}, timeout);
	}

	/**
	 * 单向发送消息
	 * @param topic topic
	 * @param payload payload
	 */
	public <T> void sendOneWayMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		// 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
		// 适用于耗时短，但对可靠性不高的场景，如日志收集
		rocketMQTemplate.sendOneWay(topic, message);
	}

	/**
	 * 延迟消息
	 * @param topic
	 * @param delay
	 * @param payload
	 */
	public <T> boolean sendDelayMessage(String topic, long delay, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSendDelayTimeSeconds(topic, payload, delay)
			.getSendStatus()
			.equals(SendStatus.SEND_OK);
	}

	/**
	 * 同步发送顺序消息
	 * @param topic topic
	 * @param payload payload
	 */
	public <T> boolean sendSyncOrderlyMessage(String topic, T payload, String id) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.syncSendOrderly(topic, message, id).getSendStatus().equals(SendStatus.SEND_OK);
	}

	/**
	 * 异步发送顺序消息
	 * @param topic topic
	 * @param payload payload
	 */
	public <T> void sendAsyncOrderlyMessage(String topic, T payload, String id) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		rocketMQTemplate.asyncSendOrderly(topic, message, id, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("发送成功");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("报错信息：{}", throwable.getMessage());
			}
		});
	}

	/**
	 * 单向发送顺序消息
	 * @param topic topic
	 * @param payload payload
	 */
	public <T> void sendOneWayOrderlyMessage(String topic, T payload, String id) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		// 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
		// 适用于耗时短，但对可靠性不高的场景，如日志收集
		rocketMQTemplate.sendOneWayOrderly(topic, message, id);
	}

	/**
	 * 事务消息
	 * @param topic
	 * @param payload
	 * @param transactionId
	 * @return
	 */
	public <T> boolean sendTransactionMessage(String topic, T payload, Long transactionId) {
		Message<T> message = MessageBuilder.withPayload(payload)
			.setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
			.build();
		return rocketMQTemplate.sendMessageInTransaction(topic, message, null)
			.getSendStatus()
			.equals(SendStatus.SEND_OK);
	}

	/**
	 * 转换并发送
	 * @param topic
	 * @param payload
	 */
	public <T> void convertAndSendMessage(String topic, T payload) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		rocketMQTemplate.convertAndSend(topic, message);
	}

	/**
	 * 发送并接收
	 */
	public <T> Object sendAndReceiveMessage(String topic, T payload, Class<?> clazz) {
		Message<T> message = MessageBuilder.withPayload(payload).build();
		return rocketMQTemplate.sendAndReceive(topic, message, clazz);
	}

	@Override
	public void afterPropertiesSet() {
		rocketMQTemplate.setAsyncSenderExecutor(taskExecutor.getThreadPoolExecutor());
	}

	private <T> void sendAsyncMessage(String topic, String tag, Message<T> message) {
		rocketMQTemplate.asyncSend(String.format(MqConstant.TOPIC_TAG, topic, tag), message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("发送成功");
			}

			@Override
			public void onException(Throwable throwable) {
				log.error("报错信息：{}", throwable.getMessage());
			}
		});
	}

}
