/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import lombok.Getter;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.BigDecimalUtil;
import org.laokou.common.i18n.dto.ClientObject;
import org.laokou.common.i18n.utils.DateUtil;
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
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author laokou
 */
@Data
@Schema(name = "ServerCO", description = "服务器")
public class ServerCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 886551822597986662L;

	private static final int OSHI_WAIT_SECOND = 1000;

	@Schema(name = "cpu", description = "CPU相关信息")
	private Cpu cpu = new Cpu();

	@Schema(name = "mem", description = "內存相关信息")
	private Mem mem = new Mem();

	@Schema(name = "jvm", description = "JVM相关信息")
	private Jvm jvm = new Jvm();

	@Schema(name = "sys", description = "服务器相关信息")
	private Sys sys = new Sys();

	@Schema(name = "files", description = "磁盘相关信息")
	private List<SysFile> files = new LinkedList<>();

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
		cpu.setCpuNum(processor.getLogicalProcessorCount());
		cpu.setTotal(totalCpu);
		cpu.setSys(cSys);
		cpu.setUsed(user);
		cpu.setWait(iowait);
		cpu.setFree(idle);
	}

	/**
	 * 设置内存信息.
	 * @param memory 内存
	 */
	private void setMemInfo(GlobalMemory memory) {
		mem.setTotal(memory.getTotal());
		mem.setUsed(memory.getTotal() - memory.getAvailable());
		mem.setFree(memory.getAvailable());
	}

	/**
	 * 设置服务器信息.
	 */
	@SneakyThrows
	private void setSysInfo() {
		Properties props = System.getProperties();
		sys.setComputerName(InetAddress.getLocalHost().getHostName());
		sys.setComputerIp(InetAddress.getLocalHost().getHostAddress());
		sys.setOsName(props.getProperty("os.name"));
		sys.setOsArch(props.getProperty("os.arch"));
		sys.setUserDir(props.getProperty("user.dir"));
	}

	/**
	 * 设置Java虚拟机.
	 */
	private void setJvmInfo() {
		Properties props = System.getProperties();
		jvm.setTotal(Runtime.getRuntime().totalMemory());
		jvm.setMax(Runtime.getRuntime().maxMemory());
		jvm.setFree(Runtime.getRuntime().freeMemory());
		jvm.setVersion(props.getProperty("java.version"));
		jvm.setHome(props.getProperty("java.home"));
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
			SysFile sysFile = new SysFile();
			sysFile.setDirName(fs.getMount());
			sysFile.setSysTypeName(fs.getType());
			sysFile.setTypeName(fs.getName());
			sysFile.setTotal(convertFileSize(total));
			sysFile.setFree(convertFileSize(free));
			sysFile.setUsed(convertFileSize(used));
			sysFile.setUsage(BigDecimalUtil.multiply(BigDecimalUtil.divide(used, total, 4), 100));
			files.add(sysFile);
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

@Data
@Schema(name = "Jvm", description = "JVM")
class Jvm extends ClientObject {

	@Serial
	private static final long serialVersionUID = 6661783699792848234L;

	@Schema(name = "total", description = "当前JVM占用的内存总数(M)")
	private double total;

	@Schema(name = "max", description = "JVM最大可用内存总数(M)")
	private double max;

	@Schema(name = "free", description = "JVM空闲内存(M)")
	private double free;

	@Getter
	@Schema(name = "version", description = "JDK版本")
	private String version;

	@Getter
	@Schema(name = "home", description = "JDK路径")
	private String home;

	public double getTotal() {
		return BigDecimalUtil.divide(total, (1024 * 1024), 2);
	}

	public double getMax() {
		return BigDecimalUtil.divide(max, (1024 * 1024), 2);
	}

	public double getFree() {
		return BigDecimalUtil.divide(free, (1024 * 1024), 2);
	}

	public double getUsed() {
		return BigDecimalUtil.divide(total - free, (1024 * 1024), 2);
	}

	public double getUsage() {
		return BigDecimalUtil.multiply(BigDecimalUtil.divide(total - free, total, 4), 100);
	}

	/**
	 * 获取JDK名称.
	 */
	public String getName() {
		return ManagementFactory.getRuntimeMXBean().getVmName();
	}

	/**
	 * JDK启动时间.
	 */
	public String getStartTime() {
		long timestamp = ManagementFactory.getRuntimeMXBean().getStartTime();
		LocalDateTime localDateTime = DateUtil.getLocalDateTimeOfTimestamp(timestamp);
		return DateUtil.format(localDateTime, DateUtil.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS);
	}

	/**
	 * JDK运行时间.
	 */
	public String getRunTime() {
		long timestamp = ManagementFactory.getRuntimeMXBean().getStartTime();
		LocalDateTime start = DateUtil.getLocalDateTimeOfTimestamp(timestamp);
		LocalDateTime end = DateUtil.now();
		long day = DateUtil.getDays(start, end);
		long hour = DateUtil.getHours(start, end) - 24 * day;
		long min = DateUtil.getMinutes(start, end) - 60 * hour - 60 * 24 * day;
		return day + "天" + hour + "小时" + min + "分钟";
	}

}

@Data
@Schema(name = "Cpu", description = "CPU")
class Cpu extends ClientObject {

	@Serial
	private static final long serialVersionUID = 8621293532430186793L;

	@Schema(name = "cpuNum", description = "核心数")
	private int cpuNum;

	@Schema(name = "total", description = "CPU总的使用率")
	private double total;

	@Schema(name = "sys", description = "CPU系统总数")
	private double sys;

	@Schema(name = "used", description = "CPU用户使用率")
	private double used;

	@Schema(name = "wait", description = "CPU当前等待率")
	private double wait;

	@Schema(name = "free", description = "CPU当前空闲率")
	private double free;

	public double getTotal() {
		return BigDecimalUtil.round(BigDecimalUtil.multiply(total, 100), 2);
	}

	public double getSys() {
		return BigDecimalUtil.round(BigDecimalUtil.multiply(sys / total, 100), 2);
	}

	public double getUsed() {
		return BigDecimalUtil.round(BigDecimalUtil.multiply(used / total, 100), 2);
	}

	public double getWait() {
		return BigDecimalUtil.round(BigDecimalUtil.multiply(wait / total, 100), 2);
	}

	public double getFree() {
		return BigDecimalUtil.round(BigDecimalUtil.multiply(free / total, 100), 2);
	}

}

@Data
@Schema(name = "Mem", description = "内存")
class Mem extends ClientObject {

	@Serial
	private static final long serialVersionUID = 4618498208469144168L;

	@Schema(name = "total", description = "内存总量")
	private double total;

	@Schema(name = "used", description = "已用内存")
	private double used;

	@Schema(name = "free", description = "剩余内存")
	private double free;

	public double getTotal() {
		return BigDecimalUtil.divide(total, (1024 * 1024 * 1024), 2);
	}

	public double getUsed() {
		return BigDecimalUtil.divide(used, (1024 * 1024 * 1024), 2);
	}

	public double getFree() {
		return BigDecimalUtil.divide(free, (1024 * 1024 * 1024), 2);
	}

	public double getUsage() {
		return BigDecimalUtil.multiply(BigDecimalUtil.divide(used, total, 4), 100);
	}

}

@Data
@Schema(name = "Sys", description = "系统")
class Sys extends ClientObject {

	@Serial
	private static final long serialVersionUID = -2249049152299436233L;

	@Schema(name = "computerName", description = "服务器名称")
	private String computerName;

	@Schema(name = "computerIp", description = "服务器IP")
	private String computerIp;

	@Schema(name = "userDir", description = "项目路径")
	private String userDir;

	@Schema(name = "osName", description = "操作系统")
	private String osName;

	@Schema(name = "osArch", description = "系统架构")
	private String osArch;

}

@Data
@Schema(name = "SysFile", description = "系统文件")
class SysFile extends ClientObject {

	@Serial
	private static final long serialVersionUID = 2307419364818519046L;

	@Schema(name = "dirName", description = "盘符路径")
	private String dirName;

	@Schema(name = "sysTypeName", description = "盘符类型")
	private String sysTypeName;

	@Schema(name = "typeName", description = "文件类型")
	private String typeName;

	@Schema(name = "total", description = "总大小")
	private String total;

	@Schema(name = "free", description = "剩余大小")
	private String free;

	@Schema(name = "used", description = "已经使用量")
	private String used;

	@Schema(name = "usage", description = "资源的使用率")
	private double usage;

}
