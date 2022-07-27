package io.laokou.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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

    public final Long getKeysSize() {
        final Object obj2 = redisTemplate.execute((RedisCallback) connection -> connection.dbSize());
        return null == obj2 ? 0 : Long.valueOf(obj2.toString());
    }

    public final Long getUsedMemory() {
        final Object obj1 = redisTemplate.execute((RedisCallback) connection -> Optional.ofNullable(connection.info("memory")).orElseThrow(RuntimeException::new).get("used_memory"));
        return null == obj1 ? 0 : Long.valueOf(obj1.toString());
    }

}
