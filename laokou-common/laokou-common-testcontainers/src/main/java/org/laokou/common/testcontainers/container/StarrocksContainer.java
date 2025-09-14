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

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.ContainerLessJdbcDelegate;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * @author laokou
 */
public class StarrocksContainer extends GenericContainer<StarrocksContainer> {

	private String password;

	private String database;

	private List<String> initScriptPaths;

	public StarrocksContainer(DockerImageName dockerImageName) {
		super(dockerImageName);
        this.withExposedPorts(8030);
		this.withExposedPorts(8040);
		this.withExposedPorts(9030);
	}

	@Override
	protected void containerIsStarted(InspectContainerResponse containerInfo) {
        try {
			Thread.sleep(Duration.ofSeconds(10));
			ExecResult execResult = this.execInContainer("/bin/sh", "-c", "mysql -h 127.0.0.1 -P 9030 -u root -p --execute=\"ALTER USER 'root'@'%' IDENTIFIED BY '" + password + "';\"");
			if (execResult.getExitCode() != 0) {
				throw new RuntimeException(execResult.getStderr());
			}
			execResult = this.execInContainer("/bin/sh", "-c", "mysql -h 127.0.0.1 -P 9030 -u root -p" + password + " -e \"CREATE DATABASE " + database + ";\"");
			if (execResult.getExitCode() != 0) {
				throw new RuntimeException(execResult.getStderr());
			}
			// 运行脚本
			runInitScriptIfRequired();
		} catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

	public StarrocksContainer withPassword(String password) {
		this.password = password;
		return this;
	}

	public StarrocksContainer withDatabase(String database) {
		this.database = database;
		return this;
	}

	public StarrocksContainer withScriptPaths(String ...paths) {
		this.initScriptPaths = List.of(paths);
		return this;
	}

	private void runInitScriptIfRequired() {
		initScriptPaths
				.stream()
				.filter(Objects::nonNull)
				.forEach(path -> ScriptUtils.runInitScript(new ContainerLessJdbcDelegate(getConnection()), path));
	}

	private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + this.getHost() + ":" + this.getMappedPort(9030) + "/" + database, "root", password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
