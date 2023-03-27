package org.laokou.test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = {
        "org.laokou.common.rocketmq"
        ,"org.laokou.**"
})
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
