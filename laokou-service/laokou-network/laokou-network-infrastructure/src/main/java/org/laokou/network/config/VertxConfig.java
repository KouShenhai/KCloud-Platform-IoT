/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.network.config;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Configuration
public class VertxConfig {

	@Bean(destroyMethod = "close")
	public Vertx vertx() {
		VertxOptions vertxOptions = new VertxOptions();
		vertxOptions.setMaxEventLoopExecuteTime(30);
		vertxOptions.setWorkerPoolSize(40);
		vertxOptions.setMaxWorkerExecuteTime(30);
		vertxOptions.setMaxEventLoopExecuteTimeUnit(TimeUnit.SECONDS);
		vertxOptions.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);
		vertxOptions.setPreferNativeTransport(true);
		vertxOptions.setInternalBlockingPoolSize(40);
		vertxOptions.setEventLoopPoolSize(Math.max(32, 2 * CpuCoreSensor.availableProcessors()));
		return Vertx.vertx(vertxOptions);
	}

}
