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

package org.laokou.admin.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.dept.Dept;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;
import java.util.List;

import static org.laokou.common.i18n.common.StringConstants.COMMA;
import static org.laokou.common.i18n.common.TenantConstants.DEFAULT;

/**
 * 部门管理.
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	private final TransactionalUtil transactionalUtil;

	private final DeptConvertor deptConvertor;

	/**
	 * 查询部门列表
	 * @param dept 部门对象
	 * @return 部门列表
	 */
	@Override
	public List<Dept> list(Dept dept) {
		DeptDO deptDO = deptConvertor.toDataObject(dept);
		return deptConvertor.convertEntityList(deptMapper.getDeptList(deptDO));
	}

	/**
	 * 新增部门
	 * @param dept 部门对象
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(Dept dept) {
		DeptDO deptDO = deptConvertor.toDataObject(dept);
		deptDO.setId(IdGenerator.defaultSnowflakeId());
		deptDO.setPath(getPath(deptDO.getPid(), deptDO.getId()));
		return insertDept(deptDO);
	}

	/**
	 * 修改部门
	 * @param dept 部门对象
	 * @return 修改结果
	 */
	@Override
	public Boolean update(Dept dept) {
		DeptDO deptDO = deptConvertor.toDataObject(dept);
		DeptDO dep = deptMapper.selectById(deptDO.getId());
		deptDO.setVersion(dep.getVersion());
		deptDO.setPath(getPath(deptDO.getPid(), deptDO.getId()));
		// 获取所有子节点
		List<DeptDO> deptChildrenList = deptMapper.selectDeptChildrenListByLikePath(dep.getPath());
		return updateDept(deptDO, dept.getPath(), deptDO.getPath(), deptChildrenList);
	}

	/**
	 * 根据角色ID查看部门IDS
	 * @param roleId 角色IDS
	 * @return 部门IDS
	 */
	@Override
	public List<Long> getDeptIds(Long roleId) {
		return deptMapper.getDeptIdsByRoleId(roleId);
	}

	/**
	 * 根据ID删除部门
	 * @param id ID
	 * @return 删除结果
	 */
	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return deptMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 根据ID查看部门
	 * @param id ID
	 * @return 部门
	 */
	@Override
	public Dept getById(Long id) {
		return deptConvertor.convertEntity(deptMapper.selectById(id));
	}

	/**
	 * 修改部门
	 * @param deptDO 部门数据模型
	 * @param oldPath 旧部门PATH
	 * @param newPath 新部门PATH
	 * @param deptChildrenList 部门子节点列表
	 * @return 修改结果
	 */
	public Boolean updateDept(DeptDO deptDO, String oldPath, String newPath, List<DeptDO> deptChildrenList) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				deptMapper.updateById(deptDO);
				updateDeptChildren(oldPath, newPath, deptChildrenList);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增部门
	 * @param deptDO 部门数据模型
	 * @return 新增结果
	 */
	private Boolean insertDept(DeptDO deptDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return deptMapper.insertTable(deptDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 修改部门子节点
	 * @param oldPath 旧部门PATH
	 * @param newPath 新部门PATH
	 * @param deptChildrenList 部门子节点列表
	 */
	private void updateDeptChildren(String oldPath, String newPath, List<DeptDO> deptChildrenList) {
		if (CollectionUtil.isNotEmpty(deptChildrenList)) {
			deptChildrenList.forEach(deptChild -> {
				deptChild.setPath(deptChild.getPath().replace(oldPath, newPath));
				deptMapper.updateById(deptChild);
			});
		}
	}

	/**
	 * 查看部门PATH
	 * @param pid 部门父节点ID
	 * @param id ID
	 * @return 部门PATH
	 */
	private String getPath(Long pid, Long id) {
		String path = deptMapper.getDeptPathByPid(pid);
		return StringUtil.isNotEmpty(path) ? path + COMMA + id : DEFAULT + COMMA + id;
	}

}
