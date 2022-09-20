package io.laokou.gateway;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author kou shen hai
 */
@SpringBootApplication(scanBasePackages = {"io.laokou.common","io.laokou.gateway"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableEncryptableProperties
@EnableApolloConfig
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * 监控服务
     * @param applicationName
     * @return
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
