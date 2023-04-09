package tech.powerjob.server;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.context.annotation.PropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * powerjob-server entry
 *
 * @author tjq
 * @since 2020/3/29
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
@PropertySource("/application.yml")
@EnableEncryptableProperties
public class PowerJobApplication {

    private static final String TIPS = "\n\n" +
            "******************* PowerJob Tips *******************\n" +
            "如果应用无法启动，我们建议您仔细阅读以下文档来解决:\n" +
            "if server can't startup, we recommend that you read the documentation to find a solution:\n" +
            "https://www.yuque.com/powerjob/guidence/problem\n" +
            "******************* PowerJob Tips *******************\n\n";

    public static void main(String[] args) {
        // Start SpringBoot application.
        try {
            SpringApplication.run(PowerJobApplication.class, args);
        } catch (Throwable t) {
            log.error(TIPS);
            throw t;
        }
    }

}
