/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.flowable.domain.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.util.Map;

/**
 * @author laokou
 */
@Data
@Schema(name = "Audit", description = "审批")
public class Audit extends AggregateRoot<Long> {

	@Schema(name = "taskId", description = "任务ID")
	private String taskId;

	@Schema(name = "values", description = "流程变量")
	private Map<String, Object> values;

	public void checkTask(Object obj) {
		if (ObjectUtil.isNull(obj)) {
			throw new RuntimeException("任务不存在");
		}
	}

	public void checkPending(boolean pending) {
		if (pending) {
			throw new RuntimeException("非审批任务，请处理任务");
		}
	}

}
