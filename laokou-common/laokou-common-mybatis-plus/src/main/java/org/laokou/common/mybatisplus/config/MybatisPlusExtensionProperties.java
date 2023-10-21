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

package org.laokou.common.mybatisplus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

import static com.baomidou.mybatisplus.core.toolkit.Constants.MYBATIS_PLUS;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = MYBATIS_PLUS)
public class MybatisPlusExtensionProperties {

	public static final String SLOW_SQL = "slow-sql";

	private SlowSql slowSql = new SlowSql();
	private Tenant tenant = new Tenant();

	@Data
	public static class Tenant {
		private boolean enabled = false;
		private Set<String> ignoreTables = Collections.emptySet();
	}

	@Data
	public static class SlowSql {

		private boolean enabled;

		private Duration millis = Duration.ofMillis(500);

	}

}
