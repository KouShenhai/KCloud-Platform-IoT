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
package org.laokou.rocketmq.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.laokou.rocketmq.client.dto.RocketmqDTO;
import org.springframework.web.bind.annotation.*;

/**
 * @author laokou
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class RocketProducer {

    private final RocketMQTemplate rocketMQTemplate;

    /**
     * rocketmq消息>同步发送
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/send/{topic}")
    public void sendMessage(@PathVariable("topic") String topic, @RequestBody RocketmqDTO dto) {
        rocketMQTemplate.syncSend(topic, dto);
    }

    /**
     * rocketmq消息>异步发送
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendAsync/{topic}")
    public void sendAsyncMessage(@PathVariable("topic") String topic, @RequestBody RocketmqDTO dto) {
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
     * rocketmq消息>单向发送
     *
     * @param topic topic
     * @param dto   dto
     */
    @PostMapping("/sendOne/{topic}")
    public void sendOneMessage(@PathVariable("topic") String topic, @RequestBody RocketmqDTO dto) {
        //单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
        //适用于耗时短，但对可靠性不高的场景，如日志收集
        rocketMQTemplate.sendOneWay(topic, dto);
    }

    /**
     * rocketmq消息>延迟
     * @param topic
     * @param delay
     * @param dto
     */
    @PostMapping("/sendDelay/{topic}/{delay}")
    public void sendDelay(@PathVariable("topic") String topic,@PathVariable("delay")long delay, @RequestBody RocketmqDTO dto) {
        rocketMQTemplate.syncSendDelayTimeSeconds(topic,dto,delay);
    }

}
