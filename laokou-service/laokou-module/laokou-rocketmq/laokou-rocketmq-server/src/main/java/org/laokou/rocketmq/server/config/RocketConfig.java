package org.laokou.rocketmq.server.config;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author laokou
 */
@Configuration
@RefreshScope
public class RocketConfig {

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Bean(name = "rocketTemplate")
    public RocketMQTemplate rocketTemplate() {
        RocketMQTemplate template = new RocketMQTemplate();
        DefaultMQProducer defaultProducer = new DefaultMQProducer();
        defaultProducer.setProducerGroup(producerGroup);
        defaultProducer.setNamesrvAddr(nameServer);
        template.setProducer(defaultProducer);
        return template;
    }

}
