/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.nacos;

import com.alibaba.nacos.NacosServerBasicApplication;
import com.alibaba.nacos.NacosServerWebApplication;
import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
import com.alibaba.nacos.sys.env.Constants;
import com.alibaba.nacos.sys.env.DeploymentType;
import com.alibaba.nacos.sys.env.EnvUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import static com.alibaba.nacos.sys.env.Constants.STANDALONE_MODE_PROPERTY_NAME;

/**
 * Nacos starter.
 * <p>
 * Use @SpringBootApplication and @ComponentScan at the same time, using CUSTOM type
 * filter to control module enabled.
 * </p>
 *
 * @author nacos
 */
@Slf4j
@SpringBootApplication
@EnableEncryptableProperties
class NacosApp {

	static void main(String[] args) {
		// @formatter:off
		// -Dnacos.home => Nacos的根目录
		// Nacos控制台 => http://【ip:8848】/nacos
		// -Dnacos.standalone=true
		// -Dcom.google.protobuf.use_unsafe_pre22_gencode
		// @formatter:on
		System.setProperty("com.google.protobuf.use_unsafe_pre22_gencode", "true");
		String standalone = System.getProperty(STANDALONE_MODE_PROPERTY_NAME, "");
		if (StringUtils.isBlank(standalone)) {
			System.setProperty(STANDALONE_MODE_PROPERTY_NAME, "true");
		}

		String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
		DeploymentType deploymentType = DeploymentType.getType(type);
		EnvUtil.setDeploymentType(deploymentType);

		// Nacos Core
		NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
		ConfigurableApplicationContext coreContext = new SpringApplicationBuilder(NacosServerBasicApplication.class)
			.web(WebApplicationType.NONE)
			.banner(new ResourceBanner(new ClassPathResource("banner.txt")))
			.run(args);

		// Nacos Web
		NacosStartUpManager.start(NacosStartUp.WEB_START_UP_PHASE);
		new SpringApplicationBuilder(NacosServerWebApplication.class).parent(coreContext)
			.banner(new ResourceBanner(new ClassPathResource("banner.txt")))
			.run(args);

		// Nacos Console
		NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
		new SpringApplicationBuilder(NacosConsole.class).parent(coreContext)
			.banner(new ResourceBanner(new ClassPathResource("banner.txt")))
			.run(args);
	}

}
