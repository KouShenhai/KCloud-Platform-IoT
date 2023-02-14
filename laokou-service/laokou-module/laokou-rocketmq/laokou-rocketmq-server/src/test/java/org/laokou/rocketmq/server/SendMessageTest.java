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

package org.laokou.rocketmq.server;

import com.alibaba.cloud.stream.binder.rocketmq.constant.RocketMQConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest(classes = RocketmqApplication.class)
public class SendMessageTest {

    @Autowired
    private StreamBridge streamBridge;

    @Test
    public void mailTest() {
        Message<String> message = MessageBuilder.withPayload("2413176044@qq.com")
                .setHeader(RocketmqConstant.TAG,RocketmqConstant.MAIL_TAG)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
        streamBridge.send(RocketmqConstant.LAOKOU_MAIL_TOPIC,message);
    }

}
