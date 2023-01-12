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
package org.laokou.kafka.server.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.kafka.client.dto.KafkaDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
/**
 * @author laokou
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Kafka API",description = "消息队列API")
public class KafkaSender {

    private final KafkaTemplate kafkaTemplate;

    @PostMapping("/sendOne/{topic}")
    @Operation(summary = "消息队列>单向发送",description = "消息队列>单向发送")
    public void sendMessage(@PathVariable("topic") String topic, @RequestBody KafkaDTO dto) {
        kafkaTemplate.send(topic,dto.getData());
    }

}
