/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.cluster.dto;

import lombok.Data;
import org.laokou.common.i18n.dto.PageQuery;

/**
 *
 * 分页查询集群命令.
 *
 * @author laokou
 */
@Data
public class ClusterPageQry extends PageQuery {

	/**
	 * 集群名称.
	 */
	private String name;

	/**
	 * 集群标识.
	 */
	private String code;

	/**
	 * 集群备注.
	 */
	private String remark;

}
