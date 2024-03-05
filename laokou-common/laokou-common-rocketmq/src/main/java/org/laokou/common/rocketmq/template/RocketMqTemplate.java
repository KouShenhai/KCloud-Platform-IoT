/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.apis.producer.Transaction;
import org.apache.rocketmq.client.common.Pair;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.apache.rocketmq.client.support.RocketMQHeaders;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RocketMqTemplate {

	private final RocketMQClientTemplate rocketMQTemplate;

	/**
	 * 同步发送.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @return 发送结果
	 */
	public <T> CompletableFuture<SendReceipt> sendSyncMessage(String topic, T payload) {
		return rocketMQTemplate.asyncSendNormalMessage(topic, payload, future());
	}

	/**
	 * 异步发送.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @return 发送结果
	 */
	public <T> boolean syncSendNormalMessage(String topic, T payload) {
		try {
			return ObjectUtil.isNotNull(rocketMQTemplate.syncSendNormalMessage(topic, payload).getMessageId());
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 同步发送顺序消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @param messageGroup 消息分组（取模）
	 */
	public <T> boolean syncSendFifoMessage(String topic, T payload, String messageGroup) {
		try {
			return ObjectUtil
				.isNotNull(rocketMQTemplate.syncSendFifoMessage(topic, payload, messageGroup).getMessageId());
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * 异步发送顺序消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @param messageGroup 消息分组（取模）
	 */
	public <T> CompletableFuture<SendReceipt> asyncSendFifoMessage(String topic, T payload, String messageGroup) {
		return rocketMQTemplate.asyncSendFifoMessage(topic, payload, messageGroup, future());
	}

	/**
	 * 事务消息.
	 * @param topic 主题
	 * @param payload 消息
	 * @param <T> 泛型
	 * @param transactionId 事务ID
	 * @return 发送结果
	 */
	public <T> Pair<SendReceipt, Transaction> sendMessageInTransaction(String topic, T payload, Long transactionId) {
		try {
			Message<T> message = MessageBuilder.withPayload(payload)
				.setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
				.build();
			return rocketMQTemplate.sendMessageInTransaction(topic, message);
		}
		catch (Exception e) {
			throw new SystemException("RocketMQ发送事务消息失败");
		}
	}

	/**
	 * 转换并发送.
	 * @param topic 主题
	 * @param payload 消息内容
	 * @param <T> 泛型
	 */
	public <T> void convertAndSend(String topic, T payload) {
		rocketMQTemplate.convertAndSend(topic, payload);
	}

	/**
	 * 延迟消息.
	 * @param topic 主题
	 * @param delay 延迟时间
	 * @param payload 消息
	 * @param <T> 泛型
	 */
	public <T> boolean syncSendDelayMessage(String topic, T payload, long delay) {
		return ObjectUtil
			.isNotNull(rocketMQTemplate.syncSendDelayMessage(topic, payload, Duration.ofSeconds(delay)).getMessageId());
	}

	/**
	 * 延迟消息.
	 * @param topic 主题
	 * @param delay 延迟时间
	 * @param payload 消息
	 * @param <T> 泛型
	 */
	public <T> CompletableFuture<SendReceipt> asyncSendDelayMessage(String topic, T payload, long delay) {
		return rocketMQTemplate.asyncSendDelayMessage(topic, payload, Duration.ofSeconds(delay), future());
	}

	private CompletableFuture<SendReceipt> future() {
		CompletableFuture<SendReceipt> future = new CompletableFuture<>();
		return future.whenCompleteAsync((sendReceipt, throwable) -> {
			if (null != throwable) {
				log.error("RocketMQ消息发送失败，报错信息：{}，详情见日志", throwable.getMessage(), throwable);
				return;
			}
			log.info("RocketMQ消息发送成功, messageId：{}", sendReceipt.getMessageId());
		});
	}

}
