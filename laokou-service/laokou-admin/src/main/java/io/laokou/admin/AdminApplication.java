package io.laokou.admin;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
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
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
/**
 * 架构演变
 * 单机架构（两层架构）
 * 三层架构（集中式架构）
 * DDD分层架构(分布式微服务架构) > 用户接口层 应用层 领域层 基础层
 * @author Kou Shenhai
 */
@SpringBootApplication(scanBasePackages = {"io.laokou.common","io.laokou.admin","io.laokou.redis","io.laokou.log"},exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableApolloConfig
@EnableAspectJAutoProxy
@Slf4j
@EnableEncryptableProperties
@EnableHystrix
@EnableFeignClients(basePackages = "io.laokou.log")
public class AdminApplication implements CommandLineRunner, WebServerFactoryCustomizer<WebServerFactory> {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@Override
	public void run(String... args){
		log.info("日志乱码 > -Dfile.encoding=UTF-8");
		log.info("开启APR模式 > -Djava.library.path=./lib");
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
