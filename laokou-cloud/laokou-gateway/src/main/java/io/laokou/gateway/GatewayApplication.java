package io.laokou.gateway;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author kou shen hai
 */
@SpringBootApplication(scanBasePackages = {"io.laokou.common","io.laokou.gateway"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableEncryptableProperties
@EnableApolloConfig
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
