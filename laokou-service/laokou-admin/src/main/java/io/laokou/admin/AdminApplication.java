package io.laokou.admin;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * 架构演变
 * 单机架构（两层架构）
 * 三层架构（集中式架构）
 * DDD分层架构(分布式微服务架构) > 用户接口层 应用层 领域层 基础层
 * @author Kou Shenhai
 */
@SpringBootApplication(scanBasePackages = {"io.laokou.common","io.laokou.admin","io.laokou.redis"})
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableApolloConfig
@Slf4j
@EnableEncryptableProperties
@EnableScheduling
public class AdminApplication implements CommandLineRunner, WebServerFactoryCustomizer<WebServerFactory> {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@Override
	public void run(String... args){
		log.info("日志乱码 > -Dfile.encoding=UTF-8");
		log.info("开启APR模式 > -Djava.library.path=./lib");
		log.info("服务已启动 > http://192.168.62.1:8160/swagger-ui.html");
		log.info("解密前，账号：sammtd0YNechLucm2339HsIi1ywi2+HLuiKA1ucQ+sqPI8GjL5k5wMOkAKJEHiWvMIGRPdea0Oi7y4kKtxYgLEGgqW+1MAL8r8kczL7L+OsqvId9KUysnWFZ6R7YcLbcIU2/mmX47n4yUND30dSO1kQVLCoFxYjO17Is0vaEaQQ=");
		log.info("解密前，密码：QbPKyveJ78qLxTWgZRCSJco1qQURxYazmd5BQUKvlyVACBcCAsD4No6KLayucuNOdpfkBhpVJwH93EqLMoWiniwxJB5GmSC4tTMPhNCvhJ4hMuBNIQ0xPAbdBJWI276DQW4TH6kFPtZEc/9oNlcyidO8EbRF5DlyeWx7c0VM7jI=");
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

	@Override
	public void customize(WebServerFactory factory) {
		TomcatServletWebServerFactory containerFactory = (TomcatServletWebServerFactory) factory;
		containerFactory.setProtocol("org.apache.coyote.http11.Http11AprProtocol");
	}
}