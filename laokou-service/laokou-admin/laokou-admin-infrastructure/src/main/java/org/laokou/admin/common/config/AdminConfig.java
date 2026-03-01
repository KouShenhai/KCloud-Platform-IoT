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

package org.laokou.admin.common.config;

import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.id.generator.IdGenerator;
import org.laokou.common.id.generator.segment.RedisSegmentIdGenerator;
import org.laokou.common.id.generator.segment.SpringSegmentProperties;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author laokou
 */
@Configuration
public class AdminConfig {

	static {
		ForyFactory.INSTANCE.register(org.laokou.admin.menu.dto.clientobject.MenuTreeCO.class);
		ForyFactory.INSTANCE.register(org.laokou.admin.menu.dto.clientobject.MenuCO.class);
		ForyFactory.INSTANCE.register(org.laokou.admin.dept.dto.clientobject.DeptCO.class);
	}

	@Bean(name = "adminRedisSegmentIdGenerator", initMethod = "init", destroyMethod = "close")
	public IdGenerator adminRedisSegmentIdGenerator(RedisUtils redisUtils, SpringSegmentProperties springSegmentProperties) {
		return new RedisSegmentIdGenerator(redisUtils, springSegmentProperties);
	}

	@Bean(name = "adminIdGenerator")
	public org.laokou.common.i18n.common.IdGenerator adminIdGenerator(IdGenerator adminRedisSegmentIdGenerator) {
		return new org.laokou.common.i18n.common.IdGenerator() {
			@Override
			public Long getId() {
				return adminRedisSegmentIdGenerator.nextId();
			}

			@Override
			public List<Long> getIds(int num) {
				return adminRedisSegmentIdGenerator.nextIds(num);
			}
		};
	}

}
