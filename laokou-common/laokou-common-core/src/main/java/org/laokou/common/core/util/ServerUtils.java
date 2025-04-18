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

package org.laokou.common.core.util;

import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.util.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

/**
 * @author laokou
 */
public final class ServerUtils {

	private static final long SIZE_1G = 1024 * 1024 * 1024;

	private static final long SIZE_1M = 1024 * 1024;

	private ServerUtils() {
	}

	public static Server getServerInfo() throws UnknownHostException {
		return new Server();
	}

	@Data
	public static class Server {

		private final Sys sys;

		private final Jvm jvm;

		private final Memory memory;

		private final Cpu cpu;

		private final List<Disk> diskList;

		public Server() throws UnknownHostException {
			SystemInfo systemInfo = new SystemInfo();
			HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
			this.memory = new Memory(hardwareAbstractionLayer.getMemory());
			this.cpu = new Cpu(hardwareAbstractionLayer.getProcessor());
			this.diskList = systemInfo.getOperatingSystem()
				.getFileSystem()
				.getFileStores()
				.stream()
				.map(Disk::new)
				.toList();
			this.sys = new Sys(System.getProperties());
			this.jvm = new Jvm(System.getProperties());
		}

	}

	@Data
	public static class Disk {

		/**
		 * 磁盘路径.
		 */
		private final String path;

		/**
		 * 磁盘类型.
		 */
		private final String type;

		/**
		 * 文件类型.
		 */
		private final String fileType;

		/**
		 * 总大小.
		 */
		private final String total;

		/**
		 * 剩余大小.
		 */
		private final String free;

		/**
		 * 已使用大小.
		 */
		private final String used;

		/**
		 * 使用率.
		 */
		private final String usedRate;

		public Disk(OSFileStore osFileStore) {
			this.path = osFileStore.getMount();
			this.type = osFileStore.getType();
			this.fileType = osFileStore.getName();
			long totalSpace = osFileStore.getTotalSpace();
			long usableSpace = osFileStore.getUsableSpace();
			long usedSpace = totalSpace - usableSpace;
			this.total = BigDecimalUtils.divide(totalSpace, SIZE_1G, 2) + "G";
			this.free = BigDecimalUtils.divide(usableSpace, SIZE_1G, 2) + "G";
			this.used = BigDecimalUtils.divide(usedSpace, SIZE_1G, 2) + "G";
			this.usedRate = BigDecimalUtils.divide(osFileStore.getUsableSpace(), osFileStore.getTotalSpace(), 4) * 100
					+ "%";
		}

	}

	@Data
	public static class Sys {

		/**
		 * 服务器名称.
		 */
		private String serverName;

		/**
		 * 服务器IP.
		 */
		private String serverIp;

		/**
		 * 项目路径.
		 */
		private String projectDir;

		/**
		 * 系统名称.
		 */
		private String osName;

		/**
		 * 系统架构.
		 */
		private String osArch;

		public Sys(Properties properties) throws UnknownHostException {
			this.serverName = InetAddress.getLocalHost().getHostName();
			this.serverIp = InetAddress.getLocalHost().getHostAddress();
			this.projectDir = properties.getProperty("user.dir");
			this.osName = properties.getProperty("os.name");
			this.osArch = properties.getProperty("os.arch");
		}

	}

	@Data
	public static class Memory {

		/**
		 * 内存总量.
		 */
		private String total;

		/**
		 * 已用内存.
		 */
		private String used;

		/**
		 * 剩余内存.
		 */
		private String free;

		public Memory(GlobalMemory globalMemory) {
			long totalMemory = globalMemory.getTotal();
			long freeMemory = globalMemory.getAvailable();
			long usedMemory = totalMemory - freeMemory;
			total = BigDecimalUtils.divide(totalMemory, SIZE_1G, 2) + "G";
			free = BigDecimalUtils.divide(freeMemory, SIZE_1G, 2) + "G";
			used = BigDecimalUtils.divide(usedMemory, SIZE_1G, 2) + "G";
		}

	}

	@Data
	public static class Jvm {

		/**
		 * JVM总使用内存(M).
		 */
		private String totalUsedMemory;

		/**
		 * JVM最大可用内存总数(M).
		 */
		private String maxMemory;

		/**
		 * JVM空闲内存(M).
		 */
		private String freeMemory;

		/**
		 * JDK版本.
		 */
		private String version;

		/**
		 * JDK路径.
		 */
		private String path;

		public Jvm(Properties properties) {
			totalUsedMemory = BigDecimalUtils.divide(Runtime.getRuntime().totalMemory(), SIZE_1M, 2) + "M";
			maxMemory = BigDecimalUtils.divide(Runtime.getRuntime().maxMemory(), SIZE_1M, 2) + "M";
			freeMemory = BigDecimalUtils.divide(Runtime.getRuntime().freeMemory(), SIZE_1M, 2) + "M";
			version = properties.getProperty("java.version");
			path = properties.getProperty("java.home");
		}

	}

	@Data
	public static class Cpu {

		/**
		 * CPU核心数.
		 */
		private int coreNum;

		/**
		 * CPU系统使用率.
		 */
		private String sysUsedRate;

		/**
		 * CPU用户使用率.
		 */
		private String userUsedRate;

		/**
		 * CPU等待率.
		 */
		private String waitRate;

		/**
		 * CPU空闲率.
		 */
		private String freeRate;

		public Cpu(CentralProcessor centralProcessor) {
			long[] prevTicks = centralProcessor.getSystemCpuLoadTicks();
			Util.sleep(1000);
			long[] currTicks = centralProcessor.getSystemCpuLoadTicks();
			long user = currTicks[CentralProcessor.TickType.USER.getIndex()]
					- prevTicks[CentralProcessor.TickType.USER.getIndex()];
			long nice = currTicks[CentralProcessor.TickType.NICE.getIndex()]
					- prevTicks[CentralProcessor.TickType.NICE.getIndex()];
			long system = currTicks[CentralProcessor.TickType.SYSTEM.getIndex()]
					- prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
			long idle = currTicks[CentralProcessor.TickType.IDLE.getIndex()]
					- prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
			long ioWait = currTicks[CentralProcessor.TickType.IOWAIT.getIndex()]
					- prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
			long irq = currTicks[CentralProcessor.TickType.IRQ.getIndex()]
					- prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
			long softIrq = currTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
					- prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
			long steal = currTicks[CentralProcessor.TickType.STEAL.getIndex()]
					- prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
			long total = user + nice + system + idle + ioWait + irq + softIrq + steal;
			this.coreNum = centralProcessor.getLogicalProcessorCount();
			this.sysUsedRate = BigDecimalUtils.divide(system, total, 4) * 100 + "%";
			this.userUsedRate = BigDecimalUtils.divide(user, total, 4) * 100 + "%";
			this.waitRate = BigDecimalUtils.divide(ioWait, total, 4) * 100 + "%";
			this.freeRate = BigDecimalUtils.divide(idle, total, 4) * 100 + "%";
		}

	}

}
