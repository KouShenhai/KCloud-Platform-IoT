package org.laokou.test.container.config;

import lombok.extern.slf4j.Slf4j;
import org.laokou.test.container.annotation.ConditionalOnWin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ConditionConfig {

	@Bean
	@ConditionalOnWin(env = "win")
	User user() {
		return new User();
	}

	static class User {
		public User() {
			log.info("用户初始化成功");
		}
	}
}