/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dto.monitor.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.BigDecimalUtil;
import org.laokou.common.i18n.dto.ClientObject;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.io.Serial;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author laokou
 */
@Data
@Schema(name = "服务器", description = "服务器")
public class ServerCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 886551822597986662L;

	private static final int OSHI_WAIT_SECOND = 1000;

	@Schema(name = "CPU相关信息", description = "CPU相关信息")
	private CpuCO cpuCO = new CpuCO();

	@Schema(name = "內存相关信息", description = "內存相关信息")
	private MemoryCO memoryCO = new MemoryCO();

	@Schema(name = "JVM相关信息", description = "JVM相关信息")
	private JvmCO jvmCO = new JvmCO();

	@Schema(name = "服务器相关信息", description = "服务器相关信息")
	private SystemCO systemCO = new SystemCO();

	@Schema(name = "磁盘相关信息", description = "磁盘相关信息")
	private List<SystemFileCO> files = new LinkedList<>();

	public void copyTo() {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		setCpuInfo(hal.getProcessor());
		setMemInfo(hal.getMemory());
		setSysInfo();
		setJvmInfo();
		setSysFiles(si.getOperatingSystem());
	}

	/**
	 * 设置CPU信息.
	 * @param processor 处理器
	 */
	private void setCpuInfo(CentralProcessor processor) {
		// CPU信息
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		Util.sleep(OSHI_WAIT_SECOND);
		long[] ticks = processor.getSystemCpuLoadTicks();
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
		cpuCO.setCpuNum(processor.getLogicalProcessorCount());
		cpuCO.setTotal(totalCpu);
		cpuCO.setSys(cSys);
		cpuCO.setUsed(user);
		cpuCO.setWait(iowait);
		cpuCO.setFree(idle);
	}

	/**
	 * 设置内存信息.
	 * @param memory 内存
	 */
	private void setMemInfo(GlobalMemory memory) {
		memoryCO.setTotal(memory.getTotal());
		memoryCO.setUsed(memory.getTotal() - memory.getAvailable());
		memoryCO.setFree(memory.getAvailable());
	}

	/**
	 * 设置服务器信息.
	 */
	@SneakyThrows
	private void setSysInfo() {
		Properties props = System.getProperties();
		systemCO.setComputerName(InetAddress.getLocalHost().getHostName());
		systemCO.setComputerIp(InetAddress.getLocalHost().getHostAddress());
		systemCO.setOsName(props.getProperty("os.name"));
		systemCO.setOsArch(props.getProperty("os.arch"));
		systemCO.setUserDir(props.getProperty("user.dir"));
	}

	/**
	 * 设置Java虚拟机.
	 */
	private void setJvmInfo() {
		Properties props = System.getProperties();
		jvmCO.setTotal(Runtime.getRuntime().totalMemory());
		jvmCO.setMax(Runtime.getRuntime().maxMemory());
		jvmCO.setFree(Runtime.getRuntime().freeMemory());
		jvmCO.setVersion(props.getProperty("java.version"));
		jvmCO.setHome(props.getProperty("java.home"));
	}

	/**
	 * 设置磁盘信息.
	 * @param os 操作系统
	 */
	private void setSysFiles(OperatingSystem os) {
		FileSystem fileSystem = os.getFileSystem();
		List<OSFileStore> fileStores = fileSystem.getFileStores();
		for (OSFileStore fs : fileStores) {
			long free = fs.getUsableSpace();
			long total = fs.getTotalSpace();
			long used = total - free;
			SystemFileCO systemFileCO = new SystemFileCO();
			systemFileCO.setDirName(fs.getMount());
			systemFileCO.setSysTypeName(fs.getType());
			systemFileCO.setTypeName(fs.getName());
			systemFileCO.setTotal(convertFileSize(total));
			systemFileCO.setFree(convertFileSize(free));
			systemFileCO.setUsed(convertFileSize(used));
			systemFileCO.setUsage(BigDecimalUtil.multiply(BigDecimalUtil.divide(used, total, 4), 100));
			files.add(systemFileCO);
		}
	}

	/**
	 * 字节转换.
	 * @param size 字节大小
	 * @return 转换后值
	 */
	public String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		}
		else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		}
		else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		}
		else {
			return String.format("%d B", size);
		}
	}

}
