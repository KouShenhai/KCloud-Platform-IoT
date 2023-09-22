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

package org.laokou.flowable.dto.definition.clientobject;

import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

/**
 * @author laokou
 */
@Data
public class DefinitionCO extends ClientObject {

	/**
	 * 定义ID
	 */
	private String definitionId;

	/**
	 * 流程名称
	 */
	private String processName;

	/**
	 * 流程KEY
	 */
	private String processKey;

	/**
	 * 部署ID
	 */
	private String deploymentId;

	/**
	 * 流程状态 0激活 1挂起
	 */
	private Boolean isSuspended;

}
