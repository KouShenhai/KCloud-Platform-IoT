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
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.gateway.DeptGateway;
import org.laokou.auth.domain.auth.Auth;
import org.laokou.auth.gatewayimpl.database.DeptMapper;
import org.laokou.common.i18n.utils.LogUtil;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
	 * 查看部门PATHS.
	 * @param auth 用户对象
	 * @return 部门PATHS
	 */
	@Override
	public Set<String> findDeptPaths(Auth auth) {
		try {
			if (auth.isSuperAdministrator()) {
				return new HashSet<>(deptMapper.selectDeptPaths());
			}
			return new HashSet<>(deptMapper.selectDeptPathsByUserId(auth.getId()));
		}
		catch (BadSqlGrammarException e) {
			log.error("表 boot_sys_dept 不存在，错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			// throw new DataSourceException(CUSTOM_SERVER_ERROR, "表 boot_sys_dept 不存在");
			throw e;
		}
	}

}
