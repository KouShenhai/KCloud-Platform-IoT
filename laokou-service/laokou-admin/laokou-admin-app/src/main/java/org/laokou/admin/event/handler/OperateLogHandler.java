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

package org.laokou.admin.event.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.dto.log.domainevent.OperateLogEvent;
import org.laokou.admin.gatewayimpl.database.OperateLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OperateLogDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * 操作日志处理.
 *
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class OperateLogHandler implements ApplicationListener {

	private final OperateLogMapper operateLogMapper;

	private final Executor executor;

	// @Override
	// public void onApplicationEvent(OperateLogEvent event) {
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

	private void execute(OperateLogEvent event) {
		OperateLogDO operateLogDO = ConvertUtil.sourceToTarget(event, OperateLogDO.class);
		operateLogMapper.insertTable(operateLogDO);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

	}

}
