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

package org.laokou.distributed.id.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosServiceManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author laokou
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.snowflake", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SpringSnowflakeProperties.class)
class SnowflakeConfig {

	/**
	 * 创建基于 Nacos 的雪花生成器 Bean.
	 * @return NacosSnowflakeGenerator
	 */
	@Bean(initMethod = "init", destroyMethod = "close")
	public SnowflakeGenerator snowflakeGenerator(NacosConfigManager nacosConfigManager,
			NacosServiceManager nacosServiceManager, SpringSnowflakeProperties springSnowflakeProperties,
			Environment environment) {
		return new NacosSnowflakeGenerator(nacosConfigManager, nacosServiceManager, springSnowflakeProperties,
				environment);
	}

}
