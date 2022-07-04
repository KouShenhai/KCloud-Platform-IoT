package io.laokou.redis.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RedisSessionConfig {

    @Value("${spring.redis.database}")
    private Integer DATABASE;

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
        server.setDatabase(DATABASE);
        return Redisson.create(config);
    }

}
