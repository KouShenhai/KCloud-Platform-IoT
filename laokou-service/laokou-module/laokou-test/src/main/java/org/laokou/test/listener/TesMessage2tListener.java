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

package org.laokou.test.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.laokou.common.rocketmq.dto.RocketmqDTO;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
//@Component
//@RocketMQMessageListener(consumerGroup = "laokou-consumer-group-2", topic = "laokou-test-2")
public class TesMessage2tListener implements RocketMQReplyListener<RocketmqDTO,RocketmqDTO> {
    @Override
    public RocketmqDTO onMessage(RocketmqDTO dto) {
        log.info("获取消息：{}",dto.getBody());
        return new RocketmqDTO();
    }
}
