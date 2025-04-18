/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.MQAdmin;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.jetbrains.annotations.NotNull;
import org.laokou.common.core.util.ThreadUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * rocketmq支持虚拟线程池.
 *
 * @author laokou
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnClass({ MQAdmin.class })
@ConditionalOnProperty(prefix = "rocketmq", value = "name-server", matchIfMissing = true)
@Import({ MessageConverterConfiguration.class, ListenerContainerConfiguration.class,
		ExtProducerResetConfiguration.class, ExtConsumerResetConfiguration.class,
		RocketMQTransactionConfiguration.class, RocketMQListenerConfiguration.class })
@AutoConfigureAfter({ MessageConverterConfiguration.class })
@AutoConfigureBefore({ RocketMQTransactionConfiguration.class })
public class RocketMQAutoConfiguration implements ApplicationContextAware {

	public static final String ROCKETMQ_TEMPLATE_DEFAULT_GLOBAL_NAME = "rocketMQTemplate";

	public static final String PRODUCER_BEAN_NAME = "defaultMQProducer";

	public static final String CONSUMER_BEAN_NAME = "defaultLitePullConsumer";

	private final Environment environment;

	private ApplicationContext applicationContext;

	public RocketMQAutoConfiguration(Environment environment) {
		this.environment = environment;
		checkProperties();
	}

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void checkProperties() {
		String nameServer = environment.getProperty("rocketmq.name-server", String.class);
		log.debug("rocketmq.nameServer = {}", nameServer);
		if (nameServer == null) {
			log.warn(
					"The necessary spring property 'rocketmq.name-server' is not defined, all rockertmq beans creation are skipped!");
		}
	}

	@Bean(PRODUCER_BEAN_NAME)
	@ConditionalOnMissingBean(DefaultMQProducer.class)
	@ConditionalOnProperty(prefix = "rocketmq", value = { "name-server", "producer.group" })
	public DefaultMQProducer defaultMQProducer(RocketMQProperties rocketMQProperties) {
		RocketMQProperties.Producer producerConfig = rocketMQProperties.getProducer();
		String nameServer = rocketMQProperties.getNameServer();
		String groupName = producerConfig.getGroup();
		Assert.hasText(nameServer, "[rocketmq.name-server] must not be null");
		Assert.hasText(groupName, "[rocketmq.producer.group] must not be null");

		String accessChannel = rocketMQProperties.getAccessChannel();

		String ak = rocketMQProperties.getProducer().getAccessKey();
		String sk = rocketMQProperties.getProducer().getSecretKey();
		boolean isEnableMsgTrace = rocketMQProperties.getProducer().isEnableMsgTrace();
		String customizedTraceTopic = rocketMQProperties.getProducer().getCustomizedTraceTopic();

		DefaultMQProducer producer = RocketMQUtil.createDefaultMQProducer(groupName, ak, sk, isEnableMsgTrace,
				customizedTraceTopic);

		producer.setNamesrvAddr(nameServer);
		if (StringUtils.hasLength(accessChannel)) {
			producer.setAccessChannel(AccessChannel.valueOf(accessChannel));
		}
		producer.setSendMsgTimeout(producerConfig.getSendMessageTimeout());
		producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
		producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
		producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
		producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMessageBodyThreshold());
		producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.isRetryNextServer());
		producer.setUseTLS(producerConfig.isTlsEnable());
		if (StringUtils.hasText(producerConfig.getNamespace())) {
			producer.setNamespace(producerConfig.getNamespace());
		}
		if (StringUtils.hasText(producerConfig.getNamespaceV2())) {
			producer.setNamespaceV2(producerConfig.getNamespaceV2());
		}
		producer.setInstanceName(producerConfig.getInstanceName());
		// 设置虚拟线程
		producer.setAsyncSenderExecutor(ThreadUtils.newVirtualTaskExecutor());
		log.info("a producer ({}) init on namesrv {}", groupName, nameServer);
		return producer;
	}

	@Bean(CONSUMER_BEAN_NAME)
	@ConditionalOnMissingBean(DefaultLitePullConsumer.class)
	@ConditionalOnProperty(prefix = "rocketmq", value = { "name-server", "pull-consumer.group", "pull-consumer.topic" })
	public DefaultLitePullConsumer defaultLitePullConsumer(RocketMQProperties rocketMQProperties)
			throws MQClientException {
		RocketMQProperties.PullConsumer consumerConfig = rocketMQProperties.getPullConsumer();
		String nameServer = rocketMQProperties.getNameServer();
		String groupName = consumerConfig.getGroup();
		String topicName = consumerConfig.getTopic();
		Assert.hasText(nameServer, "[rocketmq.name-server] must not be null");
		Assert.hasText(groupName, "[rocketmq.pull-consumer.group] must not be null");
		Assert.hasText(topicName, "[rocketmq.pull-consumer.topic] must not be null");

		String accessChannel = rocketMQProperties.getAccessChannel();
		MessageModel messageModel = MessageModel.valueOf(consumerConfig.getMessageModel());
		SelectorType selectorType = SelectorType.valueOf(consumerConfig.getSelectorType());
		String selectorExpression = consumerConfig.getSelectorExpression();
		String ak = consumerConfig.getAccessKey();
		String sk = consumerConfig.getSecretKey();
		int pullBatchSize = consumerConfig.getPullBatchSize();
		boolean useTLS = consumerConfig.isTlsEnable();

		DefaultLitePullConsumer litePullConsumer = RocketMQUtil.createDefaultLitePullConsumer(nameServer, accessChannel,
				groupName, topicName, messageModel, selectorType, selectorExpression, ak, sk, pullBatchSize, useTLS);
		litePullConsumer.setEnableMsgTrace(consumerConfig.isEnableMsgTrace());
		litePullConsumer.setCustomizedTraceTopic(consumerConfig.getCustomizedTraceTopic());
		if (StringUtils.hasText(consumerConfig.getNamespace())) {
			litePullConsumer.setNamespace(consumerConfig.getNamespace());
		}
		if (StringUtils.hasText(consumerConfig.getNamespaceV2())) {
			litePullConsumer.setNamespaceV2(consumerConfig.getNamespaceV2());
		}
		litePullConsumer.setInstanceName(consumerConfig.getInstanceName());
		log.info("a pull consumer({} sub {}) init on namesrv {}", groupName, topicName, nameServer);
		return litePullConsumer;
	}

	@Bean(destroyMethod = "destroy")
	@Conditional(ProducerOrConsumerPropertyCondition.class)
	@ConditionalOnMissingBean(name = ROCKETMQ_TEMPLATE_DEFAULT_GLOBAL_NAME)
	public RocketMQTemplate rocketMQTemplate(RocketMQMessageConverter rocketMQMessageConverter) {
		RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
		if (applicationContext.containsBean(PRODUCER_BEAN_NAME)) {
			rocketMQTemplate.setProducer((DefaultMQProducer) applicationContext.getBean(PRODUCER_BEAN_NAME));
		}
		if (applicationContext.containsBean(CONSUMER_BEAN_NAME)) {
			rocketMQTemplate.setConsumer((DefaultLitePullConsumer) applicationContext.getBean(CONSUMER_BEAN_NAME));
		}
		// 虚拟线程
		rocketMQTemplate.setAsyncSenderExecutor(ThreadUtils.newVirtualTaskExecutor());
		rocketMQTemplate.setMessageConverter(rocketMQMessageConverter.getMessageConverter());
		return rocketMQTemplate;
	}

	static class ProducerOrConsumerPropertyCondition extends AnyNestedCondition {

		public ProducerOrConsumerPropertyCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnBean(DefaultMQProducer.class)
		static class DefaultMQProducerExistsCondition {

		}

		@ConditionalOnBean(DefaultLitePullConsumer.class)
		static class DefaultLitePullConsumerExistsCondition {

		}

	}

}
