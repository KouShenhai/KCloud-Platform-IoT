/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.auth.event.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.auth.domain.event.LoginFailedEvent;
import org.laokou.auth.domain.event.LoginSucceededEvent;
import org.laokou.auth.gatewayimpl.database.LoginLogMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.event.DecorateDomainEvent;
import org.laokou.common.domain.repository.DomainEventDO;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.common.EventTypeEnums;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.context.DynamicTableSuffixContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.ORDERLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.common.i18n.common.EventStatusEnums.CONSUME_FAILED;
import static org.laokou.common.i18n.common.EventStatusEnums.CONSUME_SUCCEED;
import static org.laokou.common.i18n.common.RocketMqConstants.LAOKOU_LOGIN_LOG_CONSUMER_GROUP;
import static org.laokou.common.i18n.common.RocketMqConstants.LAOKOU_LOGIN_LOG_TOPIC;
import static org.laokou.common.i18n.common.StringConstants.UNDER;

/**
 * 登录日志处理器.
 *
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_LOGIN_LOG_CONSUMER_GROUP, topic = LAOKOU_LOGIN_LOG_TOPIC,
		messageModel = CLUSTERING, consumeMode = ORDERLY)
public class LoginLogHandler implements RocketMQListener<MessageExt> {

	private final LoginLogMapper loginLogMapper;

	private final DomainEventService domainEventService;

	@Override
	public void onMessage(MessageExt message) {
		List<DomainEvent<Long>> events = new ArrayList<>(1);
		String msg = new String(message.getBody(), StandardCharsets.UTF_8);
		DomainEventDO eventDO = JacksonUtil.toBean(msg, DomainEventDO.class);
		try {
			switch (EventTypeEnums.valueOf(eventDO.getEventType())) {
				case LOGIN_FAILED -> {
					LoginFailedEvent event = JacksonUtil.toBean(eventDO.getAttribute(), LoginFailedEvent.class);
					create(Objects.requireNonNull(ConvertUtil.sourceToTarget(event, LoginLogDO.class)), eventDO);
				}
				case LOGIN_SUCCEEDED -> {
					LoginSucceededEvent event = JacksonUtil.toBean(eventDO.getAttribute(), LoginSucceededEvent.class);
					create(Objects.requireNonNull(ConvertUtil.sourceToTarget(event, LoginLogDO.class)), eventDO);
				}
			}
			// 消费成功
			events.add(new DecorateDomainEvent(eventDO.getId(), CONSUME_SUCCEED, eventDO.getSourceName()));
		}
		catch (Exception e) {
			log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
			if (e instanceof DataIntegrityViolationException) {
				// 消费成功（数据重复直接改为消费成功）
				events.add(new DecorateDomainEvent(eventDO.getId(), CONSUME_SUCCEED, eventDO.getSourceName()));
			} else {
				// 消费失败
				events.add(new DecorateDomainEvent(eventDO.getId(), CONSUME_FAILED, eventDO.getSourceName()));
			}
		}
		finally {
			domainEventService.modify(events);
		}
	}

	private LoginLogDO convert(LoginLogDO logDO, DomainEventDO eventDO) {
		logDO.setId(IdGenerator.defaultSnowflakeId());
		logDO.setEditor(eventDO.getEditor());
		logDO.setCreator(eventDO.getCreator());
		logDO.setCreateDate(eventDO.getCreateDate());
		logDO.setUpdateDate(eventDO.getUpdateDate());
		logDO.setDeptId(eventDO.getDeptId());
		logDO.setDeptPath(eventDO.getDeptPath());
		logDO.setTenantId(eventDO.getTenantId());
		logDO.setEventId(eventDO.getId());
		return logDO;
	}

	private void create(LoginLogDO logDO, DomainEventDO eventDO) {
		try {
			DynamicTableSuffixContextHolder.set(UNDER.concat(DateUtil.format(DateUtil.now(), DateUtil.YYYYMM)));
			loginLogMapper.insert(convert(logDO, eventDO));
		}
		finally {
			DynamicTableSuffixContextHolder.clear();
		}
	}

}
