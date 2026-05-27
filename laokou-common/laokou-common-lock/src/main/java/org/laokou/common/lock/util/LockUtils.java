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

/*
 * Copyright (c) 2013-2022 Nikita Koksharov
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.lock.util;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.lock.Lock;
import org.laokou.common.lock.RedissonLock;
import org.laokou.common.lock.Type;
import org.laokou.common.redis.util.RedisUtils;

import java.util.function.Supplier;

/**
 * @author laokou
 */
@Slf4j
public final class LockUtils {

	private LockUtils() {
	}

	public static Object executeWithLock(String key, Type lockType, long timeout, int retry, RedisUtils redisUtils,
			Supplier<Object> supplier) throws InterruptedException {
		Lock lock = new RedissonLock(redisUtils);
		boolean isLocked = false;
		try {
			do {
				// 注意：设置锁的过期时间，看门狗失效
				isLocked = lock.tryLock(lockType, key, timeout);
			}
			while (!isLocked && --retry > 0);
			if (!isLocked) {
				throw new SystemException(StatusCode.TOO_MANY_REQUESTS);
			}
			return supplier.get();
		}
		catch (Throwable throwable) {
			log.error("【分布式锁】 => 加锁失败，错误信息：{}", throwable.getMessage(), throwable);
			throw throwable;
		}
		finally {
			// 释放锁
			if (isLocked) {
				lock.unlock(lockType, key);
			}
		}
	}

}
