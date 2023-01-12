/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.redis.factory;

import lombok.extern.slf4j.Slf4j;
import org.laokou.redis.enums.LockType;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author laokou
 */
@Slf4j
public class LocalLock extends AbstractLock<Lock> {

    @Override
    public Lock getLock(LockType type, String key) {
        return switch (type) {
            case LOCK -> new ReentrantLock();
            case FAIR -> new ReentrantLock(true);
            case READ -> new ReentrantReadWriteLock().readLock();
            case WRITE -> new ReentrantReadWriteLock().writeLock();
        };
    }

    @Override
    public Boolean tryLock(Lock lock, long expire, long timeout) throws InterruptedException {
        return lock.tryLock(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void unlock(Lock lock) {
        if (lock != null) {
            if (lock instanceof ReentrantLock reentrantLock) {
                if (reentrantLock.isLocked()) {
                    if (reentrantLock.isHeldByCurrentThread()) {
                        log.info("当前线程持有锁，进行锁释放");
                        lock.unlock();
                    }
                } else {
                    log.info("线程没有持有锁，锁无需释放");
                }
            } else if (lock instanceof ReentrantReadWriteLock.WriteLock writeLock) {
                //当前线程保持锁定的个数
                if (writeLock.getHoldCount() == 0) {
                    if (writeLock.isHeldByCurrentThread()) {
                        log.info("当前线程持有锁，进行锁释放");
                        lock.unlock();
                    }
                } else {
                    log.info("当前线程保持锁定的个数不为0，锁无法释放");
                }
            } else {
                lock.unlock();
            }
        }
    }
}
