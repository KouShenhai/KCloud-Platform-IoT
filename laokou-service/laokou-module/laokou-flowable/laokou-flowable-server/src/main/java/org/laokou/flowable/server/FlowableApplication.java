package org.laokou.flowable.server;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.laokou.common.swagger.config.CorsConfig;
import org.laokou.common.swagger.exception.CustomExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = {"org.laokou.flowable","org.laokou.common.core","org.laokou.common.swagger"})
@EnableDiscoveryClient
@EnableEncryptableProperties
@Import({CorsConfig.class})
public class FlowableApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }

}
