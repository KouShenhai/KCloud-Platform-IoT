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
package org.laokou.logstash.rocketmq.listener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.rocketmq.constant.RocketmqConstant;
import org.laokou.logstash.client.index.TraceIndex;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = "laokou-consumer-group", topic = RocketmqConstant.LAOKOU_TRACE_TOPIC)
public class MessageListener implements RocketMQListener<MessageExt> {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private static final String TRACE_INDEX = "laokou_trace";

    @Override
    @SneakyThrows
    public void onMessage(MessageExt messageExt) {
        try {
            // 按月建立索引
            String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            // 清洗数据
            TraceIndex traceIndex = JacksonUtil.toBean(msg, TraceIndex.class);
            if (StringUtil.isEmpty(traceIndex.getTraceId())) {
                return;
            }
            String ym = DateUtil.format(LocalDateTime.now(), DateUtil.YYYYMM);
            String indexName = TRACE_INDEX + "_" + ym;
            elasticsearchTemplate.syncIndexAsync(null, indexName, msg);
        } catch (Exception e) {
            log.error("同步数据报错：{}",e.getMessage());
        }
    }

    /**
     * 每个月最后一天的23：50：00创建下一个的索引
     */
    @Scheduled(cron = "0 50 23 L * ?")
    @SneakyThrows
    public void scheduleCreateIndexTask() {
        // 按月建立索引
        String ym = DateUtil.format(DateUtil.plusDays(LocalDateTime.now(),1), DateUtil.YYYYMM);
        String indexAlias = TRACE_INDEX;
        String indexName = indexAlias + "_" + ym;
        elasticsearchTemplate.createAsyncIndex(indexName, indexAlias, TraceIndex.class);
    }

}