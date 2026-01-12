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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.snowflake")
public class SpringSnowflakeProperties {

	/**
	 * 是否启用雪花算法.
	 */
	private boolean enabled = true;

	/**
	 * 起始时间戳（默认：2022-06-15 00:00:00）.
	 * <p>
	 * 雪花ID的时间戳部分是相对于这个起始时间的偏移量。 设置一个较近的起始时间可以让ID更短，但不能超过当前时间。 一旦设定不建议修改，否则可能产生重复ID。
	 * </p>
	 */
	private long startTimestamp = 1655222400000L;

	/**
	 * 批量生成ID的最大数量.
	 */
	private int maxBatchSize = 1000;

}
