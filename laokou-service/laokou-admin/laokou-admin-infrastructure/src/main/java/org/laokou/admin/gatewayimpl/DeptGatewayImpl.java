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

package org.laokou.admin.gatewayimpl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.dept.Dept;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.mybatisplus.utils.IdUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.admin.common.BizCode.ID_NOT_NULL;
import static org.laokou.common.i18n.common.Constant.COMMA;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public List<Dept> list(Dept dept, Long tenantId) {
		List<DeptDO> list = deptMapper.getDeptListByTenantIdAndLikeName(tenantId, dept.getName());
		return ConvertUtil.sourceToTarget(list, Dept.class);
	}

	@Override
	public Boolean insert(Dept dept) {
		long count = deptMapper.selectCount(Wrappers.lambdaQuery(DeptDO.class)
				.eq(DeptDO::getTenantId, UserUtil.getTenantId()).eq(DeptDO::getName, dept.getName()));
		if (count > 0) {
			throw new GlobalException("部门已存在，请重新填写");
		}
		DeptDO deptDO = DeptConvertor.toDataObject(dept);
		deptDO.setId(IdUtil.defaultId());
		deptDO.setPath(deptDO.getPath() + COMMA + deptDO.getId());
		return insertDept(deptDO);
	}

	@Override
	public Boolean update(Dept dept) {
		Long id = dept.getId();
		if (id == null) {
			throw new GlobalException(ID_NOT_NULL);
		}
		long count = deptMapper
				.selectCount(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getTenantId, UserUtil.getTenantId())
						.eq(DeptDO::getName, dept.getName()).ne(DeptDO::getId, id));
		if (count > 0) {
			throw new GlobalException("部门已存在，请重新填写");
		}
		DeptDO deptDO = DeptConvertor.toDataObject(dept);
		deptDO.setVersion(deptMapper.getVersion(id, DeptDO.class));
		deptDO.setPath(deptDO.getPath() + COMMA + deptDO.getId());
		return updateDept(deptDO);
	}

	@Override
	public List<Long> getDeptIds(Long roleId) {
		return deptMapper.getDeptIdsByRoleId(roleId);
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(r -> {
			try {
				return deptMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	@Override
	public Dept getById(Long id) {
		DeptDO deptDO = deptMapper.selectById(id);
		return ConvertUtil.sourceToTarget(deptDO, Dept.class);
	}

	private Boolean updateDept(DeptDO deptDO) {
		return transactionalUtil.execute(r -> {
			try {
				return deptMapper.updateById(deptDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean insertDept(DeptDO deptDO) {
		return transactionalUtil.execute(r -> {
			try {
				return deptMapper.insert(deptDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

}
