package org.laokou.test.webflux;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author laokou
 */
@SpringBootApplication
public class WebfluxTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebfluxTestApplication.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);    }

}
