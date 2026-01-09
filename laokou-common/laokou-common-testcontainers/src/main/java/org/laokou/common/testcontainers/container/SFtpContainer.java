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
public class SFtpContainer extends GenericContainer<SFtpContainer> {

	public SFtpContainer(DockerImageName dockerImageName) {
		super(dockerImageName);
		this.withExposedPorts(22);
	}

	public SFtpContainer withPassword(String username, String password) {
		this.withEnv("SFTP_USERS", String.format("%s:%s:::upload", username, password));
		return this;
	}

}
