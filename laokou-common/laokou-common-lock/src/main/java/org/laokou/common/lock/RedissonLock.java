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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.redisson.api.RLock;

/**
 * 分布式锁实现类.
 *
 * @author laokou
 */
@RequiredArgsConstructor
@Slf4j
public class RedissonLock extends AbstractLock<RLock> {

	private final RedisUtils redisUtils;

	/**
	 * 获取锁.
	 * @param type 锁类型
	 * @param key 键
	 * @return RLock
	 */
	@Override
	public RLock getLock(Type type, String key) {
		return type.getLock(redisUtils, key);
	}

	/**
	 * 尝试加锁.
	 * @param lock 锁
	 * @param timeout 超时时间
	 */
	@Override
	public boolean tryLock(RLock lock, long timeout) throws InterruptedException {
		// 线程名称
		String threadName = Thread.currentThread().getName();
		if (redisUtils.tryLock(lock, timeout)) {
			log.info("【分布式锁】 => 线程名称：{}，加锁成功", threadName);
			return true;
		}
		else {
			log.info("【分布式锁】 => 线程名称：{}，获取锁失败", threadName);
			return false;
		}
	}

	/**
	 * 释放锁.
	 * @param lock 锁
	 */
	@Override
	public void unlock(RLock lock) {
		if (ObjectUtils.isNotNull(lock)) {
			if (redisUtils.isLocked(lock) && redisUtils.isHeldByCurrentThread(lock)) {
				log.info("【分布式锁】 => 名称：{}，线程名：{}，开始解锁", lock.getName(), Thread.currentThread().getName());
				redisUtils.unlock(lock);
				log.info("【分布式锁】 => 解锁成功");
			}
			else {
				log.info("【分布式锁】 => 无线程持有，无需解锁");
			}
		}
	}

}
