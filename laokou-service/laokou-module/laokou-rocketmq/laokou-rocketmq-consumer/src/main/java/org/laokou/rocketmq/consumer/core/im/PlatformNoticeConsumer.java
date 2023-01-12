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

package org.laokou.rocketmq.consumer.core.im;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.im.client.PushMsgDTO;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.laokou.rocketmq.client.dto.MsgDTO;
import org.laokou.rocketmq.client.enums.ChannelTypeEnum;
import org.laokou.rocketmq.consumer.feign.im.ImApiFeignClient;
import org.laokou.rocketmq.consumer.filter.MessageFilter;
import org.springframework.stereotype.Component;
/**
 * @author laokou
 */
@RocketMQMessageListener(consumerGroup = "laokou-consumer-group-4", topic = RocketmqConstant.LAOKOU_MESSAGE_NOTICE_TOPIC)
@Component
@RequiredArgsConstructor
@Slf4j
public class PlatformNoticeConsumer implements RocketMQListener<MessageExt> {

    private final ImApiFeignClient imApiFeignClient;
    private final MessageFilter messageFilter;

    @Override
    public void onMessage(MessageExt message) {
        String messageBody = messageFilter.getBody(message);
        if (messageBody == null) {
            return;
        }
        final MsgDTO dto = JacksonUtil.toBean(messageBody, MsgDTO.class);
        if (ChannelTypeEnum.PLATFORM.ordinal() == dto.getSendChannel()) {
            try {
                PushMsgDTO pushMsgDTO = new PushMsgDTO();
                pushMsgDTO.setReceiver(dto.getReceiver());
                pushMsgDTO.setMsg(dto.getTitle());
                HttpResult<Boolean> result = imApiFeignClient.push(pushMsgDTO);
                if (!result.success()) {
                    throw new CustomException("消费失败");
                }
            } catch (FeignException e) {
                log.error("错误信息：{}",e.getMessage());
                throw new CustomException("消费失败");
            } catch (RuntimeException e) {
                log.error("错误信息:{}",e.getMessage());
                throw new CustomException("消费失败");
            }
        }
    }
}
