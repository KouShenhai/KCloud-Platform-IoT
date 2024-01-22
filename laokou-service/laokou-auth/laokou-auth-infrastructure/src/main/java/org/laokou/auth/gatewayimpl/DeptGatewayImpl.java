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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.DeptGateway;
import org.laokou.auth.gatewayimpl.database.DeptMapper;
import org.laokou.common.security.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	/**
	 * 查看部门PATHS.
	 * @param user 用户对象
	 * @return 部门PATHS
	 */
	@Override
	public List<String> getDeptPaths(User user) {
		Long userId = user.getId();
		if (user.isSuperAdmin()) {
			return deptMapper.getDeptPaths();
		}
		return deptMapper.getDeptPathsByUserId(userId);
	}

}
