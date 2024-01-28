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
import org.laokou.auth.domain.event.LoginSucceededEvent;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.ORDERLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.common.i18n.common.RocketMqConstants.LAOKOU_LOGIN_LOG_CONSUMER_GROUP;
import static org.laokou.common.i18n.common.RocketMqConstants.LAOKOU_LOGIN_LOG_TOPIC;

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

	// private final LoginLogMapper loginLogMapper;

	private final Executor executor;

	// @Override
	// public void onApplicationEvent(LoginLogEvent event) {
	// String sourceName = UserContextHolder.get().getSourceName();
	// CompletableFuture.runAsync(() -> {
	// try {
	// DynamicDataSourceContextHolder.push(sourceName);
	// execute(event);
	// }
	// catch (Exception e) {
	// log.error("数据插入失败，错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
	// }
	// finally {
	// DynamicDataSourceContextHolder.clear();
	// }
	// }, executor);
	// }

	private void execute(LoginSucceededEvent event) {
		// LoginLogDO logDO = ConvertUtil.sourceToTarget(event, LoginLogDO.class);
		// Assert.isTrue(ObjectUtil.isNotNull(logDO), "logDO is null");
		// //logDO.setCreator(event.getUserId());
		// //logDO.setEditor(event.getUserId());
		// loginLogMapper.insertDynamicTable(logDO,
		// TableTemplate.getCreateLoginLogTableSqlScript(DateUtil.now()),
		// UNDER.concat(DateUtil.format(DateUtil.now(), DateUtil.YYYYMM)));
	}

	@Override
	public void onMessage(MessageExt message) {
		log.info(new String(message.getBody(), StandardCharsets.UTF_8));
	}

}
