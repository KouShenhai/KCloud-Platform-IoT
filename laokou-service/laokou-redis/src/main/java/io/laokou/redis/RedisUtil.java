package io.laokou.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/**
 * Redis工具类
 * @author  Kou Shenhai
 */
@Component
public final class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    /**  默认过期时长为24小时，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24L;
    /**  过期时长为1小时，单位：秒 */
    public final static long HOUR_ONE_EXPIRE = 60 * 60 * 1L;
    /**  过期时长为6小时，单位：秒 */
    public final static long HOUR_SIX_EXPIRE = 60 * 60 * 6L;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1L;

    public final void set(String key, String value, long expire){
        redisTemplate.opsForValue().set(key, value);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final void set(String key, String value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public final String get(String key, long expire) {
        String value = redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
        return value;
    }

    public final String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public final void delete(String key) {
        redisTemplate.delete(key);
    }

    public final void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public final Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public final Map<String, Object> hGetAll(String key){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public final void hMSet(String key, Map<String, Object> map){
        hMSet(key, map, DEFAULT_EXPIRE);
    }

    public final void hMSet(String key, Map<String, Object> map, long expire){
        redisTemplate.opsForHash().putAll(key, map);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final void hSet(String key, String field, String value) {
        hSet(key, field, value, DEFAULT_EXPIRE);
    }

    public final void hSet(String key, String field, String value, long expire) {
        redisTemplate.opsForHash().put(key, field, value);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final void expire(String key, long expire){
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public final void hDel(String key, Object... fields){
        redisTemplate.opsForHash().delete(key, fields);
    }

    public final void leftPush(String key, String value){
        leftPush(key, value, DEFAULT_EXPIRE);
    }

    public final void leftPush(String key, String value, long expire){
        redisTemplate.opsForList().leftPush(key, value);
        if(expire != NOT_EXPIRE){
            expire(key, expire);
        }
    }

    public final String rightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取分布式锁
     * @return 是否获取到锁
     */
    public final boolean tryLock(String key){
        //利用lambda
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            //设置过期时间
            long expireAt = System.currentTimeMillis() + DEFAULT_EXPIRE + 1;
            //使用setNx判断是否有该key,有则返回false，无则返回true
            Boolean acquire = connection.setNX(key.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire){
                return true;
            }else{
                //解决死锁的问题 -> 程序突然崩溃会出现死锁 -> 不能释放锁
                //获取值
                byte[] value = connection.get(key.getBytes());
                if (Objects.nonNull(value) && value.length > 0){
                    long expireTime = Long.parseLong(new String(value));
                    if (expireTime < System.currentTimeMillis()){
                        //如果锁已经过期 getSet返回旧值
                        //在某个时刻，缓存过期啦，返回旧值是上一个key的值，显示的时间还是过期时间，则说明该线程可以被执行，也就是说该线程获取到锁，这样保证只有一个线程执行相应操作
                        byte[] oldValue = connection.getSet(key.getBytes(),String.valueOf(System.currentTimeMillis() + DEFAULT_EXPIRE + 1).getBytes());
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    public final boolean releaseLock(String key){
        //利用lambda
        return (Boolean)redisTemplate.execute((RedisCallback) connection -> {
            byte[] value = connection.get(key.getBytes());
            //判断key是否存在，不存在就是说明已经删除
            if (Objects.nonNull(value) && value.length > 0){
                long expireTime = Long.parseLong(new String(value));
                //判断过期时间是否大于当前时间，如果过期时间没到，手动删除key
                if (expireTime >= System.currentTimeMillis()){
                    connection.del(key.getBytes());
                }
            }
            return true;
        });
    }
}
