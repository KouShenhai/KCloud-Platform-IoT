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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.gateway.DeptGateway;
import org.laokou.auth.gatewayimpl.database.DeptMapper;
import org.laokou.auth.model.UserE;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	/**
	 * 查看部门路径集合.
	 * @param user 用户对象
	 * @return 部门路径集合
	 */
	@Override
	public List<String> getDeptPaths(UserE user) {
		if (user.isSuperAdministrator()) {
			return deptMapper.selectDeptPaths();
		}
		return deptMapper.selectDeptPathsByUserId(user.getId());
	}

}
