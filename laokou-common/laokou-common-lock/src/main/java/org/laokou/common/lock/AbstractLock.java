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

package org.laokou.common.lock;

/**
 * 分布式锁抽象类.
 *
 * @author laokou
 */
public abstract class AbstractLock<T> implements Lock {

	/**
	 * 尝试加锁.
	 *
	 * @param type    类型
	 * @param key     键
	 * @param expire  过期时间
	 * @param timeout 锁等待超时时间
	 * @return Boolean
	 * @throws InterruptedException 线程中断异常
	 */
	@Override
	public Boolean tryLock(Type type, String key, long expire, long timeout) throws InterruptedException {
		return tryLock(getLock(type, key), expire, timeout);
	}

	/**
	 * 释放锁.
	 *
	 * @param type 锁类型
	 * @param key  键
	 */
	@Override
	public void unlock(Type type, String key) {
		unlock(getLock(type, key));
	}

	/**
	 * 获取锁.
	 *
	 * @param type 锁类型
	 * @param key  键
	 * @return T
	 */
	public abstract T getLock(Type type, String key);

	/**
	 * 尝试加锁.
	 *
	 * @param lock    锁
	 * @param expire  过期时间
	 * @param timeout 线程等待超时时间
	 * @return Boolean
	 * @throws InterruptedException 线程中断异常
	 */
	public abstract Boolean tryLock(T lock, long expire, long timeout) throws InterruptedException;

	/**
	 * 释放锁.
	 *
	 * @param lock 锁
	 */
	public abstract void unlock(T lock);

}
