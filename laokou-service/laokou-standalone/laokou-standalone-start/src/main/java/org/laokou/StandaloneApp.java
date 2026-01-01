/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.annotation.EnableWarmUp;
import org.laokou.common.i18n.util.SslUtils;
import org.laokou.common.security.config.TransmittableThreadLocalSecurityContextHolderStrategy;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StopWatch;

import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * 单体服务启动类. exposeProxy=true => 使用Cglib代理，在切面中暴露代理对象，进行方法增强
 *
 * @author laokou
 */
@Slf4j
@EnableWarmUp
@EnableScheduling
@EnableEncryptableProperties
@EnableConfigurationProperties
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "org.laokou")
class StandaloneApp {

	// @formatter:off
	void main(String[] args) throws UnknownHostException, NoSuchAlgorithmException, KeyManagementException {
		StopWatch stopWatch = new StopWatch("Standalone应用程序");
		stopWatch.start();
		// SpringSecurity 子线程读取父线程的上下文
		SecurityContextHolder.setContextHolderStrategy(new TransmittableThreadLocalSecurityContextHolderStrategy());
		// 忽略SSL认证
		SslUtils.ignoreSSLTrust();
		new SpringApplicationBuilder(StandaloneApp.class).web(WebApplicationType.SERVLET).run(args);
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
	}
	// @formatter:on

}
