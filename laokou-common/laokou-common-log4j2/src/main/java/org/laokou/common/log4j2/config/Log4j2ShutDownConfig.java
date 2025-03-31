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

package org.laokou.common.log4j2.config;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
public class Log4j2ShutDownConfig implements ApplicationListener<ApplicationReadyEvent> {

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("优雅关闭Log4j2");
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			// 等待异步队列清空，最多60秒
			context.stop(60, TimeUnit.SECONDS);
		}));
	}

}
