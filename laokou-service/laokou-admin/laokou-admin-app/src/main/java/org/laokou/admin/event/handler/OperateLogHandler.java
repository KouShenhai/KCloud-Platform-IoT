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

package org.laokou.admin.event.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.dto.log.domainevent.OperateLogEvent;
import org.laokou.admin.gatewayimpl.database.OperateLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OperateLogDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class OperateLogHandler implements ApplicationListener<OperateLogEvent> {

	private final OperateLogMapper operateLogMapper;

	private final ThreadPoolTaskExecutor taskExecutor;

	@Override
	@Async
	public void onApplicationEvent(OperateLogEvent event) {
		CompletableFuture.runAsync(() -> {
			try {
				execute(event);
			}
			catch (Exception e) {
				log.error("数据插入失败，错误信息：{}", e.getMessage());
			}
		}, taskExecutor);
	}

	private void execute(OperateLogEvent event) {
		OperateLogDO operateLogDO = ConvertUtil.sourceToTarget(event, OperateLogDO.class);
		operateLogMapper.insertTable(operateLogDO);
	}

}
