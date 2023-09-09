/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.flowable.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class ResolveDTO implements Serializable {

	@NotBlank(message = "任务编号不为空")
	private String taskId;

	@NotBlank(message = "流程实例编号不为空")
	private String instanceId;

	@NotBlank(message = "业务编号不为空")
	private String businessKey;

	@NotBlank(message = "流程实例名称不为空")
	private String instanceName;

}
