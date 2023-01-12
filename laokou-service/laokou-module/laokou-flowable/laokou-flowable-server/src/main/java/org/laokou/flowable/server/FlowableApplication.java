package org.laokou.flowable.server;

import org.laokou.common.swagger.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = {"org.laokou.flowable","org.laokou.common.core","org.laokou.common.swagger"})
//@EnableDiscoveryClient
@Import(CorsConfig.class)
public class FlowableApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }

}
