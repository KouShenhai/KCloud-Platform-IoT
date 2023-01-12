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

package org.laokou.rocketmq.consumer.core.elasticsearch;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.elasticsearch.client.dto.ElasticsearchDTO;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.laokou.rocketmq.client.dto.SyncIndexDTO;
import org.laokou.rocketmq.consumer.feign.elasticsearch.ElasticsearchApiFeignClient;
import org.laokou.rocketmq.consumer.filter.MessageFilter;
import org.springframework.stereotype.Component;
/**
 * @author laokou
 */
@RocketMQMessageListener(consumerGroup = "laokou-consumer-group-5", topic = RocketmqConstant.LAOKOU_SYNC_INDEX_TOPIC)
@Component
@RequiredArgsConstructor
@Slf4j
public class SyncIndexConsumer implements RocketMQListener<MessageExt> {

    private final ElasticsearchApiFeignClient elasticsearchApiFeignClient;
    private final MessageFilter messageFilter;
    private final RedisUtil redisUtil;

    @Override
    public void onMessage(MessageExt message) {
        String messageBody = messageFilter.getBody(message);
        if (messageBody == null) {
            return;
        }
        try {
            SyncIndexDTO syncIndexDTO = JacksonUtil.toBean(messageBody, SyncIndexDTO.class);
            String syncIndexKey = RedisKeyUtil.getSyncIndexKey();
            Object obj = redisUtil.hGet(syncIndexKey, syncIndexDTO.getIndexName());
            if (obj != null) {
                log.info("索引{}已同步，请稍后再同步");
                return;
            }
            ElasticsearchDTO dto = ConvertUtil.sourceToTarget(syncIndexDTO, ElasticsearchDTO.class);
            HttpResult<Boolean> result = elasticsearchApiFeignClient.syncBatch(dto);
            if (!result.success()) {
                throw new CustomException("消费失败");
            }
            // 写入redis,一个小时才能同步索引
            redisUtil.hSet(syncIndexKey,syncIndexDTO.getIndexName(),"0",RedisUtil.HOUR_ONE_EXPIRE);
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
            throw new CustomException("消费失败");
        } catch (RuntimeException e) {
            log.error("错误信息:{}",e.getMessage());
            throw new CustomException("消费失败");
        }
    }
}
