/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.testcontainers.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * @author laokou
 */
public class NacosContainer extends GenericContainer<NacosContainer> {

	public NacosContainer(DockerImageName dockerImageName) {
		this(dockerImageName, 38848, 39848);
	}

	public NacosContainer(DockerImageName dockerImageName, int adminPort, int grpcPort) {
		super(dockerImageName);
		// 根据Nacos的设计，gRPC客户端端口为主端口加1000，即如果主端口为8849，则gRPC端口默认为9849
		this.addFixedExposedPort(adminPort, 8848);
		this.addExposedPort(8080);
		this.addFixedExposedPort(grpcPort, 9848);
		// 单机启动
		this.withEnv("MODE", "standalone");
		// Nacos 用于生成JWT Token的密钥，使用长度大于32字符的字符串，再经过Base64编码。
		this.withEnv("NACOS_AUTH_TOKEN", "U2VjcmV0S2V5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5");
		// Nacos Server端之间 Inner API的身份标识的Key，必填。
		this.withEnv("NACOS_AUTH_IDENTITY_KEY", "serverIdentity");
		// Nacos Server端之间 Inner API的身份标识的Value，必填。
		this.withEnv("NACOS_AUTH_IDENTITY_VALUE", "security");
		// Wait until the Nacos server is ready to accept requests.
		// Visit the login page to verify if nacos is running.
		this.setWaitStrategy(Wait.forHttp("/#/login").forPort(8080).forStatusCode(200));
	}

	public String getServerAddr() {
		return String.format("%s:%s", this.getHost(), this.getMappedPort(8848));
	}

}
