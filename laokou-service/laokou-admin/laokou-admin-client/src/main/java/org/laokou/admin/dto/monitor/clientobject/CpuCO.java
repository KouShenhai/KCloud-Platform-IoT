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
import org.laokou.common.core.utils.BigDecimalUtil;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;

/**
 * @author laokou
 */
@Data
@Schema(name = "CPU", description = "CPU")
class CpuCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = 8621293532430186793L;

	@Schema(name = "核心数", description = "核心数")
	private int cpuNum;

	@Schema(name = "CPU总的使用率", description = "CPU总的使用率")
	private double total;

	@Schema(name = "CPU系统总数", description = "CPU系统总数")
	private double sys;

	@Schema(name = "CPU用户使用率", description = "CPU用户使用率")
	private double used;

	@Schema(name = "CPU当前等待率", description = "CPU当前等待率")
	private double wait;

	@Schema(name = "CPU当前空闲率", description = "CPU当前空闲率")
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
