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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.apache.rocketmq.client.support.RocketMQHeaders;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class RocketMqTemplate {

    private static final String TOPIC_TAG = "%s:%s";

    private final RocketMQClientTemplate rocketMQClientTemplate;

    /**
     * 异步发送消息.
     *
     * @param topic   主题
     * @param tag     标签
     * @param payload 消息
     * @param traceId 链路ID
     * @param <T>     泛型
     */
    public <T> void sendAsyncMessage(String topic, String tag, T payload, String traceId) {
        Message<T> message = MessageBuilder.withPayload(payload).setHeader(TRACE_ID, traceId).build();
        sendAsyncMessage(topic, tag, message);
    }

    /**
     * 异步发送消息.
     *
     * @param topic   主题
     * @param payload 消息
     * @param traceId 链路ID
     * @param <T>     泛型
     */
    public <T> void sendAsyncMessage(String topic, T payload, String traceId) {
        Message<T> message = MessageBuilder.withPayload(payload).setHeader(TRACE_ID, traceId).build();
        sendAsyncMessage(topic, message);
    }

    /**
     * 事务消息.
     *
     * @param topic         主题
     * @param payload       消息
     * @param transactionId 事务ID
     * @param traceId       链路ID
     * @param <T>           泛型
     */
    @SneakyThrows
    public <T> void sendTransactionMessage(String topic, String tag, T payload, Long transactionId, Long traceId) {
        Message<T> message = MessageBuilder.withPayload(payload)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                .setHeader(TRACE_ID, traceId)
                .build();
        rocketMQClientTemplate.sendMessageInTransaction(getTopicTag(topic, tag), message);
    }

    private <T> void sendAsyncMessage(String topic, String tag, Message<T> message) {

    }

    private <T> void sendAsyncMessage(String topic, Message<T> message) {

    }

    private String getTopicTag(String topic, String tag) {
        return StringUtil.isEmpty(tag) ? topic : String.format(TOPIC_TAG, topic, tag);
    }

}
