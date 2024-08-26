/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.model.RoleE;
import org.springframework.stereotype.Component;
import org.laokou.admin.role.gateway.RoleGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMapper;

import java.util.Arrays;

import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.admin.role.convertor.RoleConvertor;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;

/**
 * 角色网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleGatewayImpl implements RoleGateway {

	private final RoleMapper roleMapper;

	private final TransactionalUtil transactionalUtil;

	public void create(RoleE roleE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				roleMapper.insert(RoleConvertor.toDataObject(roleE));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(RoleE roleE) {
		RoleDO roleDO = RoleConvertor.toDataObject(roleE);
		roleDO.setVersion(roleMapper.selectVersion(roleE.getId()));
		update(roleDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				roleMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(RoleDO roleDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				roleMapper.updateById(roleDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("修改失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

}
