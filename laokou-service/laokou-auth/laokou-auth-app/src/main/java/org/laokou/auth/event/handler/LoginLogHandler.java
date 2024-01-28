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
import org.laokou.auth.domain.event.LoginSucceededEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.SysConstants.THREAD_POOL_TASK_EXECUTOR_NAME;

/**
 * 登录日志处理器.
 *
 * @author laokou
 */
@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class LoginLogHandler implements ApplicationListener {

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
	public void onApplicationEvent(ApplicationEvent event) {

	}

}
