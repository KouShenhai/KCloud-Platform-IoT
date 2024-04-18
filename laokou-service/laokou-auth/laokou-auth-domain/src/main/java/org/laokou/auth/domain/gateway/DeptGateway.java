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

package org.laokou.auth.domain.gateway;

import org.laokou.auth.domain.auth.Auth;

import java.util.Set;

/**
 * 部门.
 *
 * @author laokou
 */
public interface DeptGateway {

	/**
	 * 查询部门PATHS列表.
	 * @param auth 用户对象
	 * @return 部门PATHS列表
	 */
	Set<String> findDeptPaths(Auth auth);

}
