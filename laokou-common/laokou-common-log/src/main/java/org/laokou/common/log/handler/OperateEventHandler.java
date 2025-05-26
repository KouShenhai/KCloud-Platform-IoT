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

package org.laokou.common.log.handler;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.log.convertor.OperateLogConvertor;
import org.laokou.common.log.mapper.OperateLogMapper;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.springframework.stereotype.Component;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.common.log.model.MqEnum.*;
import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * @author laokou
 */

@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = OPERATE_LOG_CONSUMER_GROUP, topic = LOG_TOPIC,
		selectorExpression = OPERATE_LOG_TAG, messageModel = CLUSTERING, consumeMode = CONCURRENTLY,
		consumeThreadMax = 128, consumeThreadNumber = 64)
public class OperateEventHandler extends AbstractDomainEventHandler {

	private final OperateLogMapper operateLogMapper;

	private final TransactionalUtils transactionalUtils;

	@Override
	protected void handleDomainEvent(DomainEvent domainEvent) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			transactionalUtils
				.executeInTransaction(() -> operateLogMapper.insert(OperateLogConvertor.toDataObject(domainEvent)));
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

}
