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

package org.laokou.common.id.generator.segment;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis 分段 ID 生成器配置属性.
 *
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.id-generator.segment")
public class SpringSegmentProperties {

	private String nodeId;

	private Map<String, SegmentConfig> configs = new HashMap<>(0);

	@Data
	public static class SegmentConfig {

		/**
		 * 每次从 Redis 获取的号段步长.
		 * <p>
		 * 步长越大，对 Redis 的访问频率越低，性能越高。 但步长过大会导致服务重启时浪费较多 ID。
		 * </p>
		 */
		private int step = 10000;

		/**
		 * Redis Key.
		 */
		private String key = "id-generator:segment";

		/**
		 * 异步加载因子.
		 * <p>
		 * 当号段使用比例达到此值时，异步加载下一个号段。 取值范围 (0, 1)，默认 0.8 表示号段用了50%时开始预加载。
		 * </p>
		 */
		private double loadFactor = 0.8;

	}

}
