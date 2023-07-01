/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysRoleApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysRoleDO;
import org.laokou.admin.server.domain.sys.entity.SysRoleDeptDO;
import org.laokou.admin.server.domain.sys.entity.SysRoleMenuDO;
import org.laokou.admin.server.domain.sys.repository.service.SysRoleDeptService;
import org.laokou.admin.server.domain.sys.repository.service.SysRoleMenuService;
import org.laokou.admin.server.domain.sys.repository.service.SysRoleService;
import org.laokou.admin.server.interfaces.qo.SysRoleQo;
import org.laokou.admin.client.dto.SysRoleDTO;
import org.laokou.admin.client.vo.SysRoleVO;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.data.filter.annotation.DataFilter;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysRoleApplicationServiceImpl implements SysRoleApplicationService {

	private final SysRoleService sysRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	private final SysRoleDeptService sysRoleDeptService;

	private final BatchUtil batchUtil;

	@Override
	@DataFilter(tableAlias = "boot_sys_role")
	public IPage<SysRoleVO> queryRolePage(SysRoleQo qo) {
		ValidatorUtil.validateEntity(qo);
		qo.setTenantId(UserUtil.getTenantId());
		IPage<SysRoleVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
		return sysRoleService.getRolePage(page, qo);
	}

	@Override
	public List<SysRoleVO> getRoleList(SysRoleQo qo) {
		qo.setTenantId(UserUtil.getTenantId());
		return sysRoleService.getRoleList(qo);
	}

	@Override
	public SysRoleVO getRoleById(Long id) {
		return sysRoleService.getRoleById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean insertRole(SysRoleDTO dto) {
		ValidatorUtil.validateEntity(dto);
		SysRoleDO roleDO = ConvertUtil.sourceToTarget(dto, SysRoleDO.class);
		long count = sysRoleService.count(Wrappers.lambdaQuery(SysRoleDO.class)
				.eq(SysRoleDO::getTenantId, UserUtil.getTenantId()).eq(SysRoleDO::getName, roleDO.getName()));
		if (count > 0) {
			throw new CustomException("角色已存在，请重新填写");
		}
		roleDO.setDeptId(UserUtil.getDeptId());
		roleDO.setTenantId(UserUtil.getTenantId());
		sysRoleService.save(roleDO);
		List<Long> menuIds = dto.getMenuIds();
		saveOrUpdate(roleDO.getId(), menuIds, dto.getDeptIds());
		return true;
	}

	private void saveOrUpdate(Long roleId, List<Long> menuIds, List<Long> deptIds) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			List<SysRoleMenuDO> roleMenuList = new ArrayList<>(menuIds.size());
			for (Long menuId : menuIds) {
				SysRoleMenuDO roleMenuDO = new SysRoleMenuDO();
				roleMenuDO.setId(IdGenerator.defaultSnowflakeId());
				roleMenuDO.setMenuId(menuId);
				roleMenuDO.setRoleId(roleId);
				roleMenuList.add(roleMenuDO);
			}
			batchUtil.insertBatch(roleMenuList, 500, sysRoleMenuService);
		}
		if (CollectionUtil.isNotEmpty(deptIds)) {
			List<SysRoleDeptDO> roleDeptList = new ArrayList<>(deptIds.size());
			for (Long deptId : deptIds) {
				SysRoleDeptDO roleDeptDO = new SysRoleDeptDO();
				roleDeptDO.setId(IdGenerator.defaultSnowflakeId());
				roleDeptDO.setDeptId(deptId);
				roleDeptDO.setRoleId(roleId);
				roleDeptList.add(roleDeptDO);
			}
			batchUtil.insertBatch(roleDeptList, 500, sysRoleDeptService);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateRole(SysRoleDTO dto) {
		ValidatorUtil.validateEntity(dto);
		Long id = dto.getId();
		if (id == null) {
			throw new CustomException("角色编号不为空");
		}
		long count = sysRoleService
				.count(Wrappers.lambdaQuery(SysRoleDO.class).eq(SysRoleDO::getTenantId, UserUtil.getTenantId())
						.eq(SysRoleDO::getName, dto.getName()).ne(SysRoleDO::getId, dto.getId()));
		if (count > 0) {
			throw new CustomException("角色已存在，请重新填写");
		}
		Integer version = sysRoleService.getVersion(id);
		SysRoleDO roleDO = ConvertUtil.sourceToTarget(dto, SysRoleDO.class);
		roleDO.setVersion(version);
		sysRoleService.updateById(roleDO);
		// 删除中间表
		List<SysRoleMenuDO> list1 = sysRoleMenuService.list(Wrappers.lambdaQuery(SysRoleMenuDO.class).eq(SysRoleMenuDO::getRoleId, dto.getId()).select(SysRoleMenuDO::getId));
		if (CollectionUtil.isNotEmpty(list1)) {
			sysRoleMenuService.removeBatchByIds(list1.stream().map(SysRoleMenuDO::getId).toList());
		}
		List<SysRoleDeptDO> list2 = sysRoleDeptService.list(Wrappers.lambdaQuery(SysRoleDeptDO.class).eq(SysRoleDeptDO::getRoleId, dto.getId()).select(SysRoleDeptDO::getId));
		if (CollectionUtil.isNotEmpty(list2)) {
			sysRoleDeptService.removeBatchByIds(list2.stream().map(SysRoleDeptDO::getId).toList());
		}
		saveOrUpdate(roleDO.getId(), dto.getMenuIds(), dto.getDeptIds());
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteRole(Long id) {
		sysRoleService.deleteRole(id);
		return true;
	}

}
