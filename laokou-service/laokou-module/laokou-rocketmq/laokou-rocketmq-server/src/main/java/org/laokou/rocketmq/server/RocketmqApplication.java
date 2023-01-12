package org.laokou.rocketmq.server;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.laokou.common.swagger.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = {"org.laokou.rocketmq.server","org.laokou.common.swagger", "org.laokou.common.core"})
@EnableDiscoveryClient
@EnableEncryptableProperties
@Import({CorsConfig.class})
public class RocketmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqApplication.class, args);
    }

}
