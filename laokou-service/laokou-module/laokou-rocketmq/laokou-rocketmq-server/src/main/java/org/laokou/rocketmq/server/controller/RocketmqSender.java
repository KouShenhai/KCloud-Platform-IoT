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
import com.alibaba.cloud.stream.binder.rocketmq.constant.RocketMQConst;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.laokou.rocketmq.client.dto.RocketmqDTO;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
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

    private final StreamBridge streamBridge;

    @PostMapping("/send/{topic}/{tag}")
    @Operation(summary = "消息队列>普通消息",description = "消息队列>普通消息")
    public void sendMessage(@PathVariable(RocketmqConstant.TOPIC) String topic, @PathVariable(RocketmqConstant.TAG)String tag, @RequestBody RocketmqDTO dto) {
        Message<String> message = MessageBuilder.withPayload(dto.getData())
                .setHeader(RocketmqConstant.TAG,tag)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
        streamBridge.send(topic,message);
    }

    @PostMapping("/sendTX/{topic}/{tag}")
    @Operation(summary = "消息队列>事务消息",description = "消息队列>事务消息")
    public void sendTXMessage(@PathVariable(RocketmqConstant.TOPIC) String topic, @PathVariable(RocketmqConstant.TAG)String tag, @RequestBody RocketmqDTO dto) {
        Message<String> message = MessageBuilder.withPayload(dto.getData())
                .setHeader(RocketMQConst.USER_TRANSACTIONAL_ARGS, RocketmqConstant.BINDER)
                .setHeader(RocketmqConstant.TAG,tag)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
        streamBridge.send(topic,message);
    }

}