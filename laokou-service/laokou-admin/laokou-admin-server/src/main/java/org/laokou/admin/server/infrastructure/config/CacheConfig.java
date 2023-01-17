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
package org.laokou.admin.server.infrastructure.config;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.laokou.admin.client.constant.CacheConstant;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * @author laokou
 */
@Configuration
public class CacheConfig {

    @Bean(name = "caffeineCacheManager")
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caffeineList = new ArrayList<>(9);
        caffeineList.add(new CaffeineCache(CacheConstant.USER, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.AUDIO, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.DEPT, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.DICT, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.IMAGE, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.MENU, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.MESSAGE, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.ROLE, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        caffeineList.add(new CaffeineCache(CacheConstant.VIDEO, Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(10)
                .maximumSize(100)
                .build()));
        cacheManager.setCaches(caffeineList);
        return cacheManager;
    }

}
