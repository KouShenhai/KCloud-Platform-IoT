package org.laokou.logstash.rocketmq.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.rocketmq.constant.RocketmqConstant;
import org.laokou.logstash.client.index.TraceIndex;
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
        // 按月建立索引
        String ym = DateUtil.format(LocalDateTime.now(), DateUtil.YYYYMM);
        String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        String indexAlias = TRACE_INDEX;
        String indexName = indexAlias + "_" + ym;
        elasticsearchTemplate.createIndex(indexName, indexAlias, TraceIndex.class);
        elasticsearchTemplate.syncIndex(null,indexName,indexAlias,msg, TraceIndex.class);
    }

}