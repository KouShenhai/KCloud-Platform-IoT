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
@Schema(name = "Mem", description = "内存")
class MemoryCO extends ClientObject {

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
