package org.laokou.logstash.rocketmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.rocketmq.constant.RocketmqConstant;
import org.laokou.logstash.client.index.TraceIndex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "laokou-consumer-group", topic = RocketmqConstant.LAOKOU_TRACE_TOPIC)
public class MessageListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("receive message：{}", JacksonUtil.toBean(msg, TraceIndex.class));
    }

}