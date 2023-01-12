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
package org.laokou.rocketmq.consumer.filter;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.common.message.MessageExt;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageFilter {

    private final RedisUtil redisUtil;

    public String getBody(MessageExt messageExt) {
        // 重试三次不成功则不进行重试
        if (messageExt.getReconsumeTimes() == RocketmqConstant.RECONSUME_TIMES) {
            return null;
        }
        String msgId = messageExt.getMsgId();
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        String messageConsumeKey = RedisKeyUtil.getMessageConsumeKey();
        Object obj = redisUtil.hGet(messageConsumeKey, msgId);
        if (obj != null) {
            return null;
        }
        redisUtil.hSet(messageConsumeKey,msgId,"0",RedisUtil.HOUR_ONE_EXPIRE);
        return body;
    }

}
