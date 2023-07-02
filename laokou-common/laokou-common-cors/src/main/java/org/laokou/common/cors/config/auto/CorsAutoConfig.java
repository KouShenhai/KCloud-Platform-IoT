/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.laokou.common.cors.config.auto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author laokou
 */
@AutoConfiguration
@Slf4j
public class CorsAutoConfig {

	@Bean
	@ConditionalOnMissingBean(CorsFilter.class)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		// 允许所有域名跨域
		config.addAllowedOriginPattern(CorsConfiguration.ALL);
		// 允许证书
		config.setAllowCredentials(true);
		// 允许所有方法
		config.addAllowedMethod(CorsConfiguration.ALL);
		// 允许任何头
		config.addAllowedHeader(CorsConfiguration.ALL);
		// 每一个小时，异步请求都发起预检请求 => 发送两次请求 第一次OPTION 第二次GET/POT/PUT/DELETE
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		configurationSource.registerCorsConfiguration("/**", config);
		log.info("cors加载完毕");
		return new CorsFilter(configurationSource);
	}

}
