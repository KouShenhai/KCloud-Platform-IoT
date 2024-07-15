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

package org.laokou.common.mybatisplus.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.mybatisplus.handler.event.SqlLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.laokou.common.core.config.TaskExecutorAutoConfig.THREAD_POOL_TASK_EXECUTOR_NAME;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class SqlLogHandler {

	@EventListener
	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	@Retryable(retryFor = Exception.class, backoff = @Backoff(delay = 2000, multiplier = 1.5))
	public void sqlLogMessage(SqlLogEvent sqlLogEvent) {
		log.info("{}", JacksonUtil.toJsonStr(sqlLogEvent));
	}

}
