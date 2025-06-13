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

package org.laokou.common.graalvmjs.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.graalvm.polyglot.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Configuration
public class GraalvmJsConfig {

	@Bean(initMethod = "init", destroyMethod = "close")
	public Executor jsExecutor(Cache<String, Value> jsCaffeine) {
		return new JsExecutor(jsCaffeine);
	}

	@Bean
	public Cache<String, Value> jsCaffeine() {
		return Caffeine.newBuilder().maximumSize(8192).expireAfterWrite(1, TimeUnit.HOURS).build();
	}

}
