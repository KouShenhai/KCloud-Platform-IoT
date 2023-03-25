/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.rocketmq.support;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.laokou.common.rocketmq.constant.RocketmqConstant;
import org.laokou.common.rocketmq.dto.RocketmqDTO;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@Slf4j
public class CustomRocketMQTemplate {
    /**
     * 同步发送消息
     * @param topic topic
     * @param dto   dto
     */
    public void sendMessage(RocketMQTemplate rocketMQTemplate, String topic, RocketmqDTO dto) {
        rocketMQTemplate.syncSend(topic, dto);
    }

    /**
     * 异步发送消息
     * @param topic topic
     * @param dto   dto
     */
    public void sendAsyncMessage(RocketMQTemplate rocketMQTemplate, String topic,  RocketmqDTO dto) {
        rocketMQTemplate.asyncSend(topic, dto, new SendCallback() {
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
     * 单向发送消息
     * @param topic topic
     * @param dto   dto
     */
    public void sendOneMessage(RocketMQTemplate rocketMQTemplate, String topic, RocketmqDTO dto) {
        //单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
        //适用于耗时短，但对可靠性不高的场景，如日志收集
        rocketMQTemplate.sendOneWay(topic, dto);
    }

    /**
     * 延迟消息
     * @param topic
     * @param delay
     * @param dto
     */
    public void sendDelay(RocketMQTemplate rocketMQTemplate, String topic,long delay, RocketmqDTO dto) {
        rocketMQTemplate.syncSendDelayTimeSeconds(topic,dto,delay);
    }

    /**
     * 同步发送顺序消息
     * @param topic topic
     * @param dto   dto
     */
    public void sendMessageOrderly(RocketMQTemplate rocketMQTemplate, String topic, RocketmqDTO dto) {
        rocketMQTemplate.syncSendOrderly(topic, dto,RocketmqConstant.LAOKOU_MESSAGE_QUEUE_SELECTOR_KEY);
    }

    /**
     * 异步发送顺序消息
     * @param topic topic
     * @param dto   dto
     */
    public void sendAsyncMessageOrderly(RocketMQTemplate rocketMQTemplate, String topic, RocketmqDTO dto) {
        rocketMQTemplate.asyncSendOrderly(topic, dto, RocketmqConstant.LAOKOU_MESSAGE_QUEUE_SELECTOR_KEY, new SendCallback() {
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
     * @param dto   dto
     */
    public void sendOneMessageOrderly(RocketMQTemplate rocketMQTemplate, String topic, RocketmqDTO dto) {
        //单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
        //适用于耗时短，但对可靠性不高的场景，如日志收集
        rocketMQTemplate.sendOneWayOrderly(topic, dto, RocketmqConstant.LAOKOU_MESSAGE_QUEUE_SELECTOR_KEY);
    }
}
