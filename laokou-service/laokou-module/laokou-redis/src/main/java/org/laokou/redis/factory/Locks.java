package org.laokou.redis.factory;
import org.laokou.redis.enums.LockType;

/**
 * @author laokou
 */
public interface Locks<T> {

    /**
     * 获取锁
     * @param type
     * @param key
     * @param expire
     * @param timeout
     * @return
     * @throws InterruptedException
     */
   Boolean tryLock(LockType type,String key, long expire, long timeout) throws InterruptedException;

    /**
     * 释放锁
     * @param type
     * @param key
     */
    void unlock(LockType type,String key);

}
