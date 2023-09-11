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

package org.laokou.flowable.dto.task.clientobject;

import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.io.Serial;
import java.util.Map;

/**
 * @author laokou
 */
@Data
public class AuditCO extends ClientObject {

	@Serial
	private static final long serialVersionUID = -945627686622034109L;

	/**
	 * 任务id
	 */
	private String taskId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 审批意见
	 */
	private String comment;

	/**
	 * 实例ID
	 */
	private String instanceId;

	/**
	 * 业务标识
	 */
	private String businessKey;

	/**
	 * 实例名称
	 */
	private String instanceName;

	/**
	 * 流程变量
	 */
	private Map<String, Object> values;

}
