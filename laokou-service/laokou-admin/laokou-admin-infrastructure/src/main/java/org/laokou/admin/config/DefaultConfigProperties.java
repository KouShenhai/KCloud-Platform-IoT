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

package org.laokou.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 默认配置.
 *
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.default-config")
public class DefaultConfigProperties {

	/**
	 * 租户前缀.
	 */
	private String tenantPrefix;

	/**
	 * 租户表集合.
	 */
	private Set<String> tenantTables = new HashSet<>(0);

	/**
	 * 优雅停机服务列表.
	 */
	private Set<String> gracefulShutdownServices = new HashSet<>(0);

	/**
	 * 域名列表.
	 */
	private Set<String> domainNames = new HashSet<>(0);

}
