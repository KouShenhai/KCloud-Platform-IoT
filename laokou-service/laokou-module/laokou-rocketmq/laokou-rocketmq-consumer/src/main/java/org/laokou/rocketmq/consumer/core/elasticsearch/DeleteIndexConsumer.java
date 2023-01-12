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
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.laokou.rocketmq.consumer.feign.elasticsearch.ElasticsearchApiFeignClient;
import org.laokou.rocketmq.consumer.filter.MessageFilter;
import org.springframework.stereotype.Component;
/**
 * @author laokou
 */
@RocketMQMessageListener(consumerGroup = "laokou-consumer-group-8", topic = RocketmqConstant.LAOKOU_DELETE_INDEX_TOPIC)
@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteIndexConsumer implements RocketMQListener<MessageExt> {

    private final ElasticsearchApiFeignClient elasticsearchApiFeignClient;
    private final MessageFilter messageFilter;
    private final RedisUtil redisUtil;

    @Override
    public void onMessage(MessageExt message) {
        String indexName = messageFilter.getBody(message);
        if (indexName == null) {
            return;
        }
        try {
            String deleteIndexKey = RedisKeyUtil.getDeleteIndexKey();
            Object obj = redisUtil.hGet(deleteIndexKey, indexName);
            if (obj != null) {
                log.info("索引{}已删除，请稍后再删除");
                return;
            }
            HttpResult<Boolean> result = elasticsearchApiFeignClient.delete(indexName);
            if (!result.success()) {
                throw new CustomException("消费失败");
            }
            // 写入redis,一个小时才能删除索引
            redisUtil.hSet(deleteIndexKey,indexName,"0",RedisUtil.HOUR_ONE_EXPIRE);
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
            throw new CustomException("消费失败");
        } catch (RuntimeException e) {
            log.error("错误信息:{}",e.getMessage());
            throw new CustomException("消费失败");
        }
    }
}
