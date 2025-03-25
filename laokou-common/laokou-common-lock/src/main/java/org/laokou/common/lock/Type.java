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

package org.laokou.common.lock;

import org.laokou.common.redis.util.RedisUtils;
import org.redisson.api.RLock;

/**
 * 锁类型枚举.
 *
 * @author laokou
 */
public enum Type {

	/**
	 * 普通锁(默认).
	 */
	LOCK {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getLock(key);
		}
	},

	/**
	 * 公平锁.
	 */
	FAIR_LOCK {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getFairLock(key);
		}
	},

	/**
	 * 读锁.
	 */
	READ_LOCK {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getReadLock(key);
		}
	},

	/**
	 * 写锁.
	 */
	WRITE_LOCK {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getWriteLock(key);
		}
	},

	/**
	 * 强一致性锁(可以解决主从延迟).
	 */
	FENCED_LOCK {
		@Override
		public RLock getLock(RedisUtils redisUtils, String key) {
			return redisUtils.getFencedLock(key);
		}
	};

	public abstract RLock getLock(RedisUtils redisUtils, String key);

}
