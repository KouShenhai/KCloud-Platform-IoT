/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.infrastructure.aspect;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.admin.client.enums.CacheEnum;
import org.laokou.admin.server.infrastructure.annotation.DataCache;
import org.laokou.common.core.utils.SpringUtil;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
/**
 * @author laokou
 */
@Component
@Aspect
@RequiredArgsConstructor
public class CacheAspect {

    private final CacheManager caffeineCacheManager;
    private final RedisUtil redisUtil;

    @Around("@annotation(org.laokou.admin.server.infrastructure.annotation.DataCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        DataCache dataCache = method.getAnnotation(DataCache.class);
        if (dataCache == null) {
            dataCache = AnnotationUtils.findAnnotation(method,DataCache.class);
        }
        long expire = dataCache.expire();
        CacheEnum type = dataCache.type();
        String name = dataCache.name();
        String key = dataCache.key();
        Object[] args = point.getArgs();
        key = RedisKeyUtil.getDoubleCacheKey(name, SpringUtil.parse(key,parameterNames,args,Long.class));
        Object value = point.proceed();
        switch (type) {
            case GET -> {
                return get(name,key,value,expire);
            }
            case PUT -> put(name,key,value,expire);
            case DEL -> del(name,key);
        }
        return value;
    }

    private void put(String name,String key ,Object value,long expire) {
        redisUtil.set(key,value,expire);
        Cache cache = caffeineCacheManager.getCache(name);
        if (cache == null) {
            throw new CustomException(String.format("请配置%s相关缓存",name));
        }
        cache.put(key,value);
    }

    private Object get(String name,String key,Object value,long expire) {
        Cache cache = caffeineCacheManager.getCache(name);
        if (cache == null) {
            throw new CustomException(String.format("请配置%s相关缓存",name));
        }
        Object obj = cache.get(key, () -> redisUtil.get(key));
        if (obj != null) {
            return obj;
        }
        redisUtil.setIfExists(key,value,expire);
        return value;
    }

    private void del(String name,String key) {
        redisUtil.delete(key);
        Cache cache = caffeineCacheManager.getCache(name);
        if (cache == null) {
            throw new CustomException(String.format("请配置%s相关缓存",name));
        }
        cache.evict(key);
    }

}
