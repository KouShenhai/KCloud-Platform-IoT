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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.ObjectUtil;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author laokou
 */
@Data
@SuperBuilder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Schema(name = "Start", description = "开始")
public class Start extends AggregateRoot<Long> {

	@Schema(name = "definitionKey", description = "定义Key")
	private String definitionKey;

	@Schema(name = "businessKey", description = "业务Key")
	private String businessKey;

	@Schema(name = "instanceName", description = "实例名称")
	private String instanceName;

	public void checkDefinition(Object obj) {
		if (ObjectUtil.isNull(obj)) {
			throw new RuntimeException("流程未定义");
		}
	}

	public void checkInstance(Object obj) {
		if (ObjectUtil.isNull(obj)) {
			throw new RuntimeException("流程不存在");
		}
	}

	public void checkSuspended(boolean suspended) {
		if (suspended) {
			throw new RuntimeException("挂起失败，流程已挂起");
		}
	}

}
