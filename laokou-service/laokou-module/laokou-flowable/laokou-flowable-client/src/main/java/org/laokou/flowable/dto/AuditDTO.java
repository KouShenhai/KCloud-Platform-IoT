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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @author laokou
 */
@Data
public class AuditDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -945627686622034109L;

	/**
	 * 任务id
	 */
	@NotBlank(message = "任务编号不为空")
	private String taskId;

	/**
	 * 任务名称
	 */
	@NotBlank(message = "任务名称不为空")
	private String taskName;

	/**
	 * 审批意见
	 */
	@NotBlank(message = "审批意见不为空")
	private String comment;

	/**
	 * 流程实例id
	 */
	@NotBlank(message = "流程实例编号不为空")
	private String instanceId;

	/**
	 * 业务key
	 */
	@NotBlank(message = "业务编号不为空")
	private String businessKey;

	/**
	 * 流程实例名称
	 */
	@NotBlank(message = "流程实例名称不为空")
	private String instanceName;

	/**
	 * 流程变量
	 */
	@NotNull(message = "流程变量不为空")
	private Map<String, Object> values;

}
