/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.rocketmq.server.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
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
@Tag(name = "RocketMQ API",description = "消息队列API")
public class RocketmqSender {

    private final RocketMQTemplate rocketTemplate;

    @PostMapping("/send/{topic}")
    @Operation(summary = "消息队列>同步发送",description = "消息队列>同步发送")
    public void sendMessage(@PathVariable("topic") String topic, @RequestBody RocketmqDTO dto) {
        SendStatus sendStatus = rocketTemplate.syncSend(topic, dto.getData()).getSendStatus();
        log.info("消息发送状态：{}",sendStatus.name());
    }

    @PostMapping("/sendAsync/{topic}")
    @Operation(summary = "消息队列>异步发送",description = "消息队列>异步发送")
    public void sendAsyncMessage(@PathVariable("topic") String topic, @RequestBody RocketmqDTO dto) {
        rocketTemplate.asyncSend(topic, dto.getData(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功");
            }
            @Override
            public void onException(Throwable throwable) {
                log.error("报错信息：{}",throwable.getMessage());
            }
        });
    }
    @PostMapping("/sendOne/{topic}")
    @Operation(summary = "消息队列>单向发送",description = "消息队列>单向发送")
    public void sendOneMessage(@PathVariable("topic") String topic, @RequestBody RocketmqDTO dto) {
         // 单向发送，只负责发送消息，不会触发回调函数，即发送消息请求不等待
         // 适用于耗时短，但对可靠性不高的场景，如日志收集
        rocketTemplate.sendOneWay(topic,dto.getData());
    }

}