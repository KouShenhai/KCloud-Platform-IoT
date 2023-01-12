package org.laokou.rocketmq.consumer;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.laokou.common.swagger.config.CorsConfig;
import org.laokou.redis.config.RedisSessionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = {"org.laokou.redis","org.laokou.common.core","org.laokou.rocketmq.consumer"})
@EnableDiscoveryClient
@EnableFeignClients
@Import({RedisSessionConfig.class, CorsConfig.class})
@EnableEncryptableProperties
public class RocketmqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqConsumerApplication.class, args);
    }

}
