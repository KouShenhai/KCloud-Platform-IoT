/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.logstash.consumer;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.logstash.common.constant.MqConstant;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RocketMQMessageListener(consumerGroup = MqConstant.LAOKOU_LOGIN_LOG_CONSUMER_GROUP,
		topic = MqConstant.LAOKOU_LOGIN_LOG_CONSUMER_GROUP_DLQ, messageModel = MessageModel.CLUSTERING,
		consumeMode = ConsumeMode.CONCURRENTLY)
public class LoginEventDLQConsumer extends AbstractDLQConsumer {

	public LoginEventDLQConsumer(RocketMqTemplate rocketMqTemplate) {
		super(rocketMqTemplate);
	}

}
