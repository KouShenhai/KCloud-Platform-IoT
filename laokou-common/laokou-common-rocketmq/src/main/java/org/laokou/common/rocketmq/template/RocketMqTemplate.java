/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import org.laokou.common.rocketmq.clientobject.MqCO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

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
	 * @param co
	 * @param timeout
	 * @return
	 */
	public boolean sendSyncMessage(String topic, MqCO co, long timeout) {
		return rocketMQTemplate.syncSend(topic, co, timeout).getSendStatus().equals(SendStatus.SEND_OK);
	}

	/**
	 * 同步发送
	 * @param topic
	 * @param co
	 * @param timeout
	 * @return
	 */
	public boolean sendSyncMessage(String topic, MqCO co, long timeout, int delayLevel) {
		Message<MqCO> payload = MessageBuilder.withPayload(co).build();
		return rocketMQTemplate.syncSend(topic, payload, timeout, delayLevel)
			.getSendStatus()
			.equals(SendStatus.SEND_OK);
	}

	/**
	 * 同步发送消息
	 * @param topic topic
	 * @param co co
	 */
	public boolean sendSyncMessage(String topic, MqCO co) {
		return rocketMQTemplate.syncSend(topic, co).getSendStatus().equals(SendStatus.SEND_OK);
	}

	/**
	 * 异步发送消息
	 * @param topic topic
	 * @param co co
	 */
	public void sendAsyncMessage(String topic, MqCO co) {
		rocketMQTemplate.asyncSend(topic, co, new SendCallback() {
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
	 * 异步发送消息
	 * @param topic topic
	 * @param tag
	 * @param co co
	 */
	public void sendAsyncMessage(String topic, String tag, MqCO co) {
		rocketMQTemplate.asyncSend(String.format(MqConstant.TOPIC_TAG, topic, tag), co, new SendCallback() {
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
	 * 异步发送消息
	 * @param topic topic
	 * @param co co
	 */
	public void sendAsyncMessage(String topic, MqCO co, long timeout) {
		rocketMQTemplate.asyncSend(topic, co, new SendCallback() {
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
	 * @param co co
	 */
	public void sendOneWayMessage(String topic, MqCO co) {
		// 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
		// 适用于耗时短，但对可靠性不高的场景，如日志收集
		rocketMQTemplate.sendOneWay(topic, co);
	}

	/**
	 * 延迟消息
	 * @param topic
	 * @param delay
	 * @param co
	 */
	public boolean sendDelayMessage(String topic, long delay, MqCO co) {
		return rocketMQTemplate.syncSendDelayTimeSeconds(topic, co, delay).getSendStatus().equals(SendStatus.SEND_OK);
	}

	/**
	 * 同步发送顺序消息
	 * @param topic topic
	 * @param co co
	 */
	public boolean sendSyncOrderlyMessage(String topic, MqCO co, String id) {
		return rocketMQTemplate.syncSendOrderly(topic, co, id).getSendStatus().equals(SendStatus.SEND_OK);
	}

	/**
	 * 异步发送顺序消息
	 * @param topic topic
	 * @param co co
	 */
	public void sendAsyncOrderlyMessage(String topic, MqCO co, String id) {
		rocketMQTemplate.asyncSendOrderly(topic, co, id, new SendCallback() {
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
	 * @param co co
	 */
	public void sendOneWayOrderlyMessage(String topic, MqCO co, String id) {
		// 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
		// 适用于耗时短，但对可靠性不高的场景，如日志收集
		rocketMQTemplate.sendOneWayOrderly(topic, co, id);
	}

	/**
	 * 事务消息
	 * @param topic
	 * @param co
	 * @param transactionId
	 * @return
	 */
	public boolean sendTransactionMessage(String topic, MqCO co, String transactionId) {
		Message<MqCO> message = MessageBuilder.withPayload(co)
			.setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
			.build();
		return rocketMQTemplate.sendMessageInTransaction(topic, message, null)
			.getSendStatus()
			.equals(SendStatus.SEND_OK);
	}

	/**
	 * 转换并发送
	 * @param topic
	 * @param co
	 */
	public void convertAndSendMessage(String topic, MqCO co) {
		rocketMQTemplate.convertAndSend(topic, co);
	}

	/**
	 * 发送并接收
	 */
	public Object sendAndReceiveMessage(String topic, MqCO co, Class<?> clazz) {
		return rocketMQTemplate.sendAndReceive(topic, co, clazz);
	}

	@Override
	public void afterPropertiesSet() {
		rocketMQTemplate.setAsyncSenderExecutor(taskExecutor.getThreadPoolExecutor());
	}

}
