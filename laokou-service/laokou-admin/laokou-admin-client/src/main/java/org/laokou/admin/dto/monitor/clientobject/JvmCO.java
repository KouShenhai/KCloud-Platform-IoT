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
import lombok.Getter;
import org.laokou.common.core.utils.BigDecimalUtil;
import org.laokou.common.i18n.dto.ClientObject;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.Serial;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
@Data
@Schema(name = "JVM", description = "JVM")
class JvmCO extends ClientObject {

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
