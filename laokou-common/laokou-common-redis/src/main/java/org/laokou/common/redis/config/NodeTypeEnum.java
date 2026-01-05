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

package org.laokou.common.redis.config;

import lombok.Getter;
import org.redisson.config.Config;

@Getter
public enum NodeTypeEnum {

	SINGLE("single", "单机模式") {
		@Override
		Config getConfig(SpringRedissonProperties springRedissonProperties) {
			SpringRedissonProperties.Single single = springRedissonProperties.getNode().getSingle();
			Config config = springRedissonProperties.createDefaultConfig();
			config.useSingleServer()
				.setAddress(single.getAddress())
				.setConnectTimeout(single.getConnectTimeout())
				.setTimeout(single.getTimeout())
				.setDatabase(single.getDatabase());
			return config;
		}
	},

	CLUSTER("cluster", "集群模式") {
		@Override
		Config getConfig(SpringRedissonProperties springRedissonProperties) {
			return null;
		}
	},

	SENTINEL("sentinel", "哨兵模式") {
		@Override
		Config getConfig(SpringRedissonProperties springRedissonProperties) {
			return null;
		}
	},

	MASTER_SLAVE("master_slave", "主从模式") {
		@Override
		Config getConfig(SpringRedissonProperties springRedissonProperties) {
			return null;
		}
	},

	REPLICATED("replicated", "复制模式") {
		@Override
		Config getConfig(SpringRedissonProperties springRedissonProperties) {
			return null;
		}
	};

	private final String code;

	private final String desc;

	NodeTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	abstract Config getConfig(SpringRedissonProperties springRedissonProperties);

}
