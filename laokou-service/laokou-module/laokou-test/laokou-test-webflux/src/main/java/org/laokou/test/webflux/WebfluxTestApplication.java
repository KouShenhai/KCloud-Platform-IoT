package org.laokou.test.webflux;
import lombok.RequiredArgsConstructor;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author laokou
 */
@SpringBootApplication
@RequiredArgsConstructor
public class WebfluxTestApplication implements CommandLineRunner {

    private final RedisUtil redisUtil;

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebfluxTestApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(redisUtil);
    }
}
