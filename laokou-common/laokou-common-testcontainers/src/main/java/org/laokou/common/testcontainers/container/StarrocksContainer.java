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
import lombok.Getter;
import org.laokou.common.i18n.common.exception.SystemException;
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

	private String databaseName;

	private List<String> initScriptPaths;

	@Getter
	private Connection connection;

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
			ExecResult execResult = this.execInContainer("/bin/sh", "-c", "mysql -h 127.0.0.1 -P 9030 -u root -p -e \"CREATE DATABASE " + databaseName + ";\"");
			if (execResult.getExitCode() != 0) {
				throw new SystemException("S_Starrocks_ExecuteShellFailed", String.format("执行Shell命令失败，错误信息：%s", execResult.getStderr()));
			}
			// 建立连接
			connect();
			// 运行脚本
			runInitScriptIfRequired();
		} catch (IOException | InterruptedException e) {
            throw new SystemException("S_Starrocks_RunScriptFailed", String.format("运行脚本失败，错误信息：%s", e.getMessage()), e);
        }
    }

	@Override
	protected void containerIsStopped(InspectContainerResponse containerInfo) {
		// 关闭连接
		closeConnect();
	}

	public StarrocksContainer withDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}

	public StarrocksContainer withScriptPaths(String ...paths) {
		this.initScriptPaths = List.of(paths);
		return this;
	}

	private void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", this.getHost() , this.getMappedPort(9030) , databaseName), "root", "");
		} catch (SQLException | ClassNotFoundException ex) {
			throw new SystemException("S_Mysql_ConnectFailed", String.format("连接Mysql失败，错误信息：%s", ex.getMessage()), ex);
		}
	}

	private void closeConnect() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			throw new SystemException("S_Mysql_CloseFailed", String.format("关闭Mysql连接失败，错误信息：%s", e.getMessage()), e);
		}
	}

	private void runInitScriptIfRequired() {
		initScriptPaths
				.stream()
				.filter(Objects::nonNull)
				.forEach(path -> ScriptUtils.runInitScript(new ContainerLessJdbcDelegate(this.connection), path));
	}

}
