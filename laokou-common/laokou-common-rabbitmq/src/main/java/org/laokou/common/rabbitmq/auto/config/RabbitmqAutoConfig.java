/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.rabbitmq.auto.config;

import org.laokou.common.rabbitmq.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author laokou
 */
@AutoConfiguration
@ComponentScan("org.laokou.common.rabbitmq")
public class RabbitmqAutoConfig {

    @Bean
    public Queue gpsInfoQueue() {
        return QueueBuilder
                // 非持久化
                .nonDurable(MqConstant.LAOKOU_GPS_INFO_QUEUE)
                .build();
    }

    @Bean
    public TopicExchange gpsInfoTopicExchange() {
        return ExchangeBuilder
                .topicExchange(MqConstant.LAOKOU_GPS_INFO_TOPIC_EXCHANGE)
                // 持久化
                .durable(true)
                // 自动删除
                .autoDelete()
                .build();
    }

    @Bean
    public Binding bindingKey1(Queue gpsInfoQueue,TopicExchange gpsInfoTopicExchange) {
        return BindingBuilder.bind(gpsInfoQueue).to(gpsInfoTopicExchange).with(MqConstant.LAOKOU_GPS_INFO_1_KEY);
    }

    @Bean
    public Binding bindingKey2(Queue gpsInfoQueue,TopicExchange gpsInfoTopicExchange) {
        return BindingBuilder.bind(gpsInfoQueue).to(gpsInfoTopicExchange).with(MqConstant.LAOKOU_GPS_INFO_2_KEY);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueueNames(MqConstant.LAOKOU_GPS_INFO_QUEUE);
        return simpleMessageListenerContainer;
    }

}
