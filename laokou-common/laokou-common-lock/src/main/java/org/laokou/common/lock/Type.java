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

package org.laokou.common.lock;

import lombok.Getter;
import org.laokou.common.redis.util.RedisUtils;
import org.redisson.api.RLock;

/**
 * 锁类型枚举.
 *
 * @author laokou
 */
@Getter
public enum Type {

	LOCK("lock", "普通锁") {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getLock(key);
		}
	},

	FAIR_LOCK("fair_lock", "公平锁") {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getFairLock(key);
		}
	},

	READ_LOCK("read_lock", "读锁") {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getReadLock(key);
		}
	},

	WRITE_LOCK("write_lock", "写锁") {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getWriteLock(key);
		}
	},

	FENCED_LOCK("fenced_lock", "强一致性锁(可以解决主从延迟)") {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getFencedLock(key);
		}
	};

	private final String code;

	private final String desc;

	Type(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract RLock getLock(RedisUtils redisUtils, String key);

}
