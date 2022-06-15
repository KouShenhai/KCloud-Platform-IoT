package io.laokou.admin.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加密
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/8/13 0013 下午 10:13
 */

@Configuration
@Slf4j
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String passwordEncode;

    @Bean
    public StringEncryptor stringEncryptor() {
        log.info("秘钥：{}",passwordEncode);
        //加密
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        //应用配置
        config.setPassword(passwordEncode);
        encryptor.setConfig(config);
        return encryptor;
    }

}
