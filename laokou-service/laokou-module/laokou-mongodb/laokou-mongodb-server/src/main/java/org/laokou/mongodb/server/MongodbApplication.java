package org.laokou.mongodb.server;

import org.laokou.common.swagger.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author laokou
 */
@SpringBootApplication
@Import(CorsConfig.class)
public class MongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }

}
