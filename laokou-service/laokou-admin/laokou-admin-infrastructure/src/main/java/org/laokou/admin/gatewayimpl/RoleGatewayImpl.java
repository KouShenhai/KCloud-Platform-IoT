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
import org.laokou.admin.domain.common.Option;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.domain.role.Role;
import org.laokou.admin.gatewayimpl.database.RoleDeptMapper;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDeptDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.IdUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleGatewayImpl implements RoleGateway {

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final RoleDeptMapper roleDeptMapper;
    private final BatchUtil batchUtil;
    private final TransactionalUtil transactionalUtil;

    @Override
    public Boolean insert(Role role) {
        Long count = roleMapper.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, role.getName()));
        if (count > 0) {
            throw new GlobalException("角色已存在，请重新填写");
        }
        RoleDO roleDO = ConvertUtil.sourceToTarget(role, RoleDO.class);
        roleDO.setDeptId(UserUtil.getDeptId());
        roleDO.setTenantId(UserUtil.getTenantId());
        return insertRole(roleDO,role);
    }

    @Override
    public Boolean update() {
        return null;
    }

    @Override
    public List<Option> getOptionList() {
        List<RoleDO> list = roleMapper.getValueListOrderByDesc(RoleDO.class, "create_date", "id", "name");
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<Option> options = new ArrayList<>(list.size());
        for (RoleDO roleDO : list) {
            Option o = new Option();
            o.setLabel(roleDO.getName());
            o.setValue(String.valueOf(roleDO.getId()));
            options.add(o);
        }
        return options;
    }

    private Boolean insertRole(RoleDO roleDO,Role role) {
        return transactionalUtil.execute(rollback -> {
            try {
                return roleMapper.insert(roleDO) > 0
                        && insertRoleMenu(roleDO.getId(), role.getMenuIds())
                        && insertRoleDept(roleDO.getId(), role.getDeptIds());
            } catch (Exception e) {
                log.error("错误信息：{}", e.getMessage());
                rollback.setRollbackOnly();
                return false;
            }
        });
    }

    private Boolean insertRoleMenu(Long roleId, List<Long> menuIds) {
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<RoleMenuDO> list = new ArrayList<>(menuIds.size());
            for (Long menuId : menuIds) {
                RoleMenuDO roleMenuDO = new RoleMenuDO();
                roleMenuDO.setRoleId(roleId);
                roleMenuDO.setMenuId(menuId);
                roleMenuDO.setId(IdUtil.defaultId());
                list.add(roleMenuDO);
            }
            batchUtil.insertBatch(list, roleMenuMapper::insertBatch);
            return true;
        }
        return false;
    }

    private Boolean insertRoleDept(Long roleId,List<Long> deptIds) {
        if (CollectionUtil.isNotEmpty(deptIds)) {
            List<RoleDeptDO> list = new ArrayList<>(deptIds.size());
            for (Long deptId : deptIds) {
                RoleDeptDO roleDeptDO = new RoleDeptDO();
                roleDeptDO.setRoleId(roleId);
                roleDeptDO.setDeptId(deptId);
                roleDeptDO.setId(IdUtil.defaultId());
                list.add(roleDeptDO);
            }
            batchUtil.insertBatch(list, roleDeptMapper::insertBatch);
            return true;
        }
        return false;
    }

}
