/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.domain.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.admin.domain.resource.Resource;

/**
 * @author laokou
 */
@Schema(name = "ResourceGateway", description = "资源网关")
public interface ResourceGateway {

	/**
	 * 新增资源.
	 * @param resource 资源对象
	 * @return 新增结果
	 */
	Boolean insert(Resource resource);

	/**
	 * 修改资源.
	 * @param resource 资源对象
	 */
	void modify(Resource resource);

	/**
	 * 同步资源.
	 * @return 同步结果
	 */
	Boolean sync();

	/**
	 * 根据ID删除资源.
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

}
