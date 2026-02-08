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
import com.alibaba.nacos.common.packagescan.util.ResourceUtils;
import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
import com.alibaba.nacos.mcpregistry.NacosMcpRegistry;
import com.alibaba.nacos.sys.env.Constants;
import com.alibaba.nacos.sys.env.DeploymentType;
import com.alibaba.nacos.sys.env.EnvUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

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
		// -Dnacos.home => Nacos的根目录
		// Nacos控制台 => http://【ip:8048】/nacos
		System.setProperty(LoggingApplicationListener.CONFIG_PROPERTY,
				ResourceUtils.CLASSPATH_URL_PREFIX + "logback-spring.xml");
		String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
		DeploymentType deploymentType = DeploymentType.getType(type);
		EnvUtil.setDeploymentType(deploymentType);

		// Nacos Core
		NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
		ConfigurableApplicationContext coreContext = new SpringApplicationBuilder(NacosServerBasicApplication.class)
			.web(WebApplicationType.NONE)
			.banner(new ResourceBanner(new ClassPathResource("banner.txt")))
			.run(args);

		// Nacos Web【Nacos Server】
		if (List.of(DeploymentType.MERGED, DeploymentType.SERVER, DeploymentType.SERVER_WITH_MCP)
			.contains(deploymentType)) {
			NacosStartUpManager.start(NacosStartUp.WEB_START_UP_PHASE);
			new SpringApplicationBuilder(NacosServerWebApplication.class).parent(coreContext)
				.banner(new ResourceBanner(new ClassPathResource("banner.txt")))
				.run(args);
		}

		// MCP Registry【Nacos Server with MCP Registry】
		if (List.of(DeploymentType.MERGED, DeploymentType.SERVER_WITH_MCP).contains(deploymentType)) {
			NacosStartUpManager.start(NacosStartUp.MCP_REGISTRY_START_UP_PHASE);
			new SpringApplicationBuilder(NacosMcpRegistry.class).parent(coreContext)
				.banner(new ResourceBanner(new ClassPathResource("banner.txt")))
				.run(args);
		}

		// Nacos Console【Nacos Console】
		if (List.of(DeploymentType.MERGED, DeploymentType.CONSOLE).contains(deploymentType)) {
			NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
			new SpringApplicationBuilder(NacosConsole.class).parent(coreContext)
				.banner(new ResourceBanner(new ClassPathResource("banner.txt")))
				.run(args);
		}
	}

}
