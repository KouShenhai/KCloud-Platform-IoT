/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.role.gatewayimpl;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.laokou.admin.role.convertor.RoleConvertor;
import org.laokou.admin.role.gateway.RoleMenuGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.admin.role.model.RoleA;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleMenuGatewayImpl implements RoleMenuGateway {

	private final RoleMenuMapper roleMenuMapper;

	private final SqlSessionFactory sqlSessionFactory;

	@Override
	public void updateRoleMenu(RoleA roleA) {
		deleteRoleMenu(roleA.getRoleE().getId());
		insertRoleMenu(roleA);
	}

	@Override
	public void deleteRoleMenu(Long[] roleIds) {
		deleteRoleMenuBatch(Arrays.asList(roleIds));
	}

	private void insertRoleMenu(RoleA roleA) {
		MybatisBatch.Method<RoleMenuDO> mapperMethod = new MybatisBatch.Method<>(RoleMenuMapper.class);
		MybatisBatchUtils.execute(sqlSessionFactory, RoleConvertor.toDataObjects(roleA), mapperMethod.insert());
	}

	private void deleteRoleMenu(Long roleId) {
		roleMenuMapper.update(Wrappers.lambdaUpdate(RoleMenuDO.class)
			.set(RoleMenuDO::getDelFlag, 1)
			.set(RoleMenuDO::getVersion, 1)
			.eq(RoleMenuDO::getRoleId, roleId)
			.eq(RoleMenuDO::getVersion, 0)
			.eq(RoleMenuDO::getDelFlag, 0));
	}

	private void deleteRoleMenuBatch(List<Long> roleIds) {
		roleMenuMapper.update(Wrappers.lambdaUpdate(RoleMenuDO.class)
			.set(RoleMenuDO::getDelFlag, 1)
			.set(RoleMenuDO::getVersion, 1)
			.in(RoleMenuDO::getRoleId, roleIds)
			.eq(RoleMenuDO::getVersion, 0)
			.eq(RoleMenuDO::getDelFlag, 0));
	}

}
