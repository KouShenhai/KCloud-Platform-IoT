/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.laokou.admin.application.service.SysUserApplicationService;
import org.laokou.admin.domain.sys.entity.SysUserDO;
import org.laokou.admin.domain.sys.entity.SysUserRoleDO;
import org.laokou.admin.domain.sys.repository.service.SysRoleService;
import org.laokou.admin.domain.sys.repository.service.SysUserRoleService;
import org.laokou.admin.domain.sys.repository.service.SysUserService;
import org.laokou.admin.interfaces.vo.OptionVO;
import org.laokou.admin.interfaces.qo.SysUserQO;
import org.laokou.admin.interfaces.vo.SysUserVO;
import org.laokou.common.constant.Constant;
import org.laokou.common.password.PasswordUtil;
import org.laokou.common.user.SecurityUser;
import org.laokou.admin.interfaces.dto.SysUserDTO;
import org.laokou.common.enums.SuperAdminEnum;
import org.laokou.common.exception.CustomException;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.ConvertUtil;
import org.laokou.datasource.annotation.DataFilter;
import org.laokou.datasource.annotation.DataSource;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
@GlobalTransactional(rollbackFor = Exception.class)
public class SysUserApplicationServiceImpl implements SysUserApplicationService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    @DataSource("master")
    public Boolean updateUser(SysUserDTO dto, HttpServletRequest request) {
        Long id = dto.getId();
        if (null == id) {
            throw new CustomException("主键不存在");
        }
        Long userId = SecurityUser.getUserId(request);
        UserDetail userDetail = sysUserService.getUserDetail(id);
        UserDetail userDetail2 = sysUserService.getUserDetail(userId);
        if (SuperAdminEnum.YES.ordinal() == userDetail.getSuperAdmin() && SuperAdminEnum.YES.ordinal() != userDetail2.getSuperAdmin()) {
            throw new CustomException("只有超级管理员才能修改");
        }
        int count = sysUserService.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getUsername, dto.getUsername()).eq(SysUserDO::getDelFlag, Constant.NO).ne(SysUserDO::getId,id));
        if (count > 0) {
            throw new CustomException("账号已存在，请重新填写");
        }
        dto.setEditor(userId);
        sysUserService.updateUser(dto);
        List<Long> roleIds = dto.getRoleIds();
        //删除中间表
        sysUserRoleService.remove(Wrappers.lambdaQuery(SysUserRoleDO.class).eq(SysUserRoleDO::getUserId, dto.getId()));
        if (CollectionUtils.isNotEmpty(roleIds)) {
            saveOrUpdate(dto.getId(),roleIds);
        }
        return true;
    }

    @Override
    @DataSource("master")
    public Boolean insertUser(SysUserDTO dto, HttpServletRequest request) {
        SysUserDO sysUserDO = ConvertUtil.sourceToTarget(dto, SysUserDO.class);
        int count = sysUserService.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getUsername, sysUserDO.getUsername()).eq(SysUserDO::getDelFlag, Constant.NO));
        if (count > 0) {
            throw new CustomException("账号已存在，请重新填写");
        }
        sysUserDO.setCreator(SecurityUser.getUserId(request));
        sysUserDO.setPassword(PasswordUtil.encode(dto.getPassword()));
        sysUserService.save(sysUserDO);
        List<Long> roleIds = dto.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            saveOrUpdate(sysUserDO.getId(),roleIds);
        }
        return true;
    }

    @Override
    @DataSource("master")
    @DataFilter(tableAlias = "boot_sys_user")
    public IPage<SysUserVO> queryUserPage(SysUserQO qo) {
        IPage<SysUserVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysUserService.getUserPage(page,qo);
    }

    @Override
    @DataSource("master")
    public SysUserVO getUserById(Long id) {
        SysUserDO sysUserDO = sysUserService.getById(id);
        SysUserVO sysUserVO = ConvertUtil.sourceToTarget(sysUserDO, SysUserVO.class);
        sysUserVO.setRoleIds(sysRoleService.getRoleIdsByUserId(sysUserVO.getId()));
        return sysUserVO;
    }

    @Override
    @DataSource("master")
    public Boolean deleteUser(Long id,HttpServletRequest request) {
        Long userId = SecurityUser.getUserId(request);
        UserDetail userDetail = sysUserService.getUserDetail(id);
        UserDetail userDetail2 = sysUserService.getUserDetail(userId);
        if (SuperAdminEnum.YES.ordinal() == userDetail.getSuperAdmin() && SuperAdminEnum.YES.ordinal() != userDetail2.getSuperAdmin()) {
            throw new CustomException("只有超级管理员才能删除");
        }
        sysUserService.deleteUser(id);
        return true;
    }

    @Override
    @DataSource("master")
    public List<OptionVO> getOptionList() {
        return sysUserService.getOptionList();
    }

    private Boolean saveOrUpdate(Long userId,List<Long> roleIds) {
        List<SysUserRoleDO> doList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                sysUserRoleDO.setRoleId(roleId);
                sysUserRoleDO.setUserId(userId);
                doList.add(sysUserRoleDO);
            }
            return sysUserRoleService.saveBatch(doList);
        }
        return false;
    }

}
