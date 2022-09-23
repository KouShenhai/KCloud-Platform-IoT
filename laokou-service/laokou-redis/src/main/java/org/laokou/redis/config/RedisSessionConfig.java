/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.redis.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kou Shenhai
 */
@Configuration
@Data
public class RedisSessionConfig {

    @Value("${spring.redis.host}")
    private String HOST;

    @Value("${spring.redis.port}")
    private String PORT;

    @Value("${spring.redis.password}")
    private String PASSWORD;

    @Bean
    public RedissonClient redisClient() {
        Config config = new Config();
        SingleServerConfig server = config.useSingleServer();
        server.setAddress("redis://" + HOST + ":" + PORT);
        server.setPassword(PASSWORD);
        return Redisson.create(config);
    }

}
