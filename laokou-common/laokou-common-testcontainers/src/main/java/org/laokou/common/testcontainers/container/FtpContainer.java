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

package org.laokou.common.testcontainers.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author laokou
 */
public class FtpContainer extends GenericContainer<FtpContainer> {

	public FtpContainer(DockerImageName dockerImageName) {
		super(dockerImageName);
		this.addFixedExposedPort(21, 21);
		this.addFixedExposedPort(30000, 30000);
		this.addFixedExposedPort(30001, 30001);
		this.withEnv("PASV_ADDRESS", "127.0.0.1");
		this.withEnv("PASV_MIN_PORT", "30000");
		this.withEnv("PASV_MAX_PORT", "30001");
	}

	public FtpContainer withPassword(String password) {
		this.withEnv("FTP_PASS", password);
		return this;
	}

	public FtpContainer withUsername(String username) {
		this.withEnv("FTP_USER", username);
		return this;
	}

}
