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
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Modbus TCP/UDP 服务器容器，用于测试 Modbus 通信协议.
 *
 * @author laokou
 */
public class ModbusContainer extends GenericContainer<ModbusContainer> {

	/**
	 * Modbus 标准端口。
	 */
	public static final int MODBUS_STANDARD_PORT = 502;

	/**
	 * pymodbus-sim 默认端口。
	 */
	public static final int PYMODBUS_PORT = 5020;

	private final int port;

	private Integer fixedUdpPort = null;

	/**
	 * 使用指定的 Docker 镜像和端口创建 Modbus 容器。
	 * @param dockerImageName Docker 镜像名称
	 * @param port Modbus 端口
	 */
	private ModbusContainer(DockerImageName dockerImageName, int port) {
		super(dockerImageName);
		this.port = port;
		this.withExposedPorts(port);
		this.setWaitStrategy(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));
	}

	/**
	 * 创建 TCP Modbus 容器。
	 * @param dockerImageName 镜像名称
	 * @param port 端口
	 * @return ModbusContainer
	 */
	public static ModbusContainer tcp(DockerImageName dockerImageName, int port) {
		ModbusContainer container = new ModbusContainer(dockerImageName, port);
		container.withCommand("--protocol", "tcp", "--port", String.valueOf(port));
		return container;
	}

	/**
	 * 创建 UDP Modbus 容器。
	 * <p>
	 * 注意：UDP 不能使用 forListeningPort 等待策略，使用日志等待策略。
	 * </p>
	 * @param dockerImageName 镜像名称
	 * @param port 端口
	 * @return ModbusContainer
	 */
	public static ModbusContainer udp(DockerImageName dockerImageName, int port) {
		ModbusContainer container = new ModbusContainer(dockerImageName, port);
		// UDP 不能使用 forListeningPort，使用日志输出等待策略
		container.setWaitStrategy(
				Wait.forLogMessage(".*Starting Modbus UDP server.*", 1).withStartupTimeout(Duration.ofSeconds(60)));
		// UDP 使用固定端口
		container.addFixedExposedPort(port, port, InternetProtocol.UDP);
		container.fixedUdpPort = port;
		container.withCommand("--protocol", "udp", "--port", String.valueOf(port));
		return container;
	}

	/**
	 * 获取 Modbus TCP 映射端口。
	 * @return 映射后的端口号
	 */
	public Integer getModbusTcpPort() {
		return this.getMappedPort(port);
	}

	/**
	 * 获取 Modbus 端口。 UDP 使用固定端口，TCP 使用动态映射端口。
	 * @return 端口号
	 */
	public Integer getModbusPort() {
		if (fixedUdpPort != null) {
			return fixedUdpPort;
		}
		return this.getMappedPort(port);
	}

}
