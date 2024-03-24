package org.laokou.test.container;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class DubboProviderTestApp {

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderTestApp.class, args);
    }

}
