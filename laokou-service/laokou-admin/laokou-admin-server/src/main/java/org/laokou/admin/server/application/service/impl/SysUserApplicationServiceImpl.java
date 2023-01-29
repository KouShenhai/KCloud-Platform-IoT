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
import org.laokou.admin.client.vo.UserInfoVO;
import org.laokou.admin.server.application.service.SysUserApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysUserDO;
import org.laokou.admin.server.domain.sys.entity.SysUserRoleDO;
import org.laokou.admin.server.domain.sys.repository.service.SysRoleService;
import org.laokou.admin.server.domain.sys.repository.service.SysUserRoleService;
import org.laokou.admin.server.domain.sys.repository.service.SysUserService;
import org.laokou.admin.client.vo.OptionVO;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.admin.client.vo.SysUserVO;
import org.laokou.admin.client.dto.SysUserDTO;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.enums.SuperAdminEnum;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.data.filter.annotation.DataFilter;
import org.laokou.common.swagger.exception.CustomException;
import org.laokou.auth.client.user.UserDetail;
import org.apache.commons.collections.CollectionUtils;
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysUserApplicationServiceImpl implements SysUserApplicationService {

    private final SysUserService sysUserService;

    private final SysRoleService sysRoleService;

    private final SysUserRoleService sysUserRoleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUser(SysUserDTO dto) {
        Long id = dto.getId();
        if (null == id) {
            throw new CustomException("主键不存在");
        }
        SysUserDO sysUser = sysUserService.getById(id);
        UserDetail userDetail = UserUtil.userDetail();
        if (SuperAdminEnum.YES.ordinal() == sysUser.getSuperAdmin() && SuperAdminEnum.YES.ordinal() != userDetail.getSuperAdmin()) {
            throw new CustomException("只有超级管理员才能修改");
        }
        long count = sysUserService.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getUsername, dto.getUsername()).eq(SysUserDO::getDelFlag, Constant.NO).ne(SysUserDO::getId,id));
        if (count > 0) {
            throw new CustomException("账号已存在，请重新填写");
        }
        dto.setEditor(userDetail.getUserId());
        String password = dto.getPassword();
        if (StringUtil.isNotEmpty(password)) {
            dto.setPassword(passwordEncoder.encode(password));
        }
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertUser(SysUserDTO dto) {
        SysUserDO sysUserDO = ConvertUtil.sourceToTarget(dto, SysUserDO.class);
        long count = sysUserService.count(Wrappers.lambdaQuery(SysUserDO.class).eq(SysUserDO::getUsername, sysUserDO.getUsername()).eq(SysUserDO::getDelFlag, Constant.NO));
        if (count > 0) {
            throw new CustomException("账号已存在，请重新填写");
        }
        sysUserDO.setCreator(UserUtil.getUserId());
        sysUserDO.setPassword(passwordEncoder.encode(dto.getPassword()));
        sysUserService.save(sysUserDO);
        List<Long> roleIds = dto.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            saveOrUpdate(sysUserDO.getId(),roleIds);
        }
        return true;
    }

    @Override
    @DataFilter(tableAlias = "boot_sys_user")
    public IPage<SysUserVO> queryUserPage(SysUserQo qo) {
        IPage<SysUserVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysUserService.getUserPage(page,qo);
    }

    @Override

    public SysUserVO getUserById(Long id) {
        SysUserDO sysUserDO = sysUserService.getById(id);
        SysUserVO sysUserVO = ConvertUtil.sourceToTarget(sysUserDO, SysUserVO.class);
        sysUserVO.setRoleIds(sysRoleService.getRoleIdsByUserId(sysUserVO.getId()));
        return sysUserVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUser(Long id) {
        SysUserDO sysUser = sysUserService.getById(id);
        UserDetail userDetail = UserUtil.userDetail();
        if (SuperAdminEnum.YES.ordinal() == sysUser.getSuperAdmin() && SuperAdminEnum.YES.ordinal() != userDetail.getSuperAdmin()) {
            throw new CustomException("只有超级管理员才能删除");
        }
        sysUserService.deleteUser(id);
        return true;
    }

    @Override
    public List<OptionVO> getOptionList() {
        return sysUserService.getOptionList();
    }

    @Override
    public UserInfoVO getUserInfo() {
        UserDetail userDetail = UserUtil.userDetail();
        return ConvertUtil.sourceToTarget(userDetail, UserInfoVO.class);
    }

    private void saveOrUpdate(Long userId, List<Long> roleIds) {
        List<SysUserRoleDO> doList = new ArrayList<>(roleIds.size());
        if (CollectionUtils.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                sysUserRoleDO.setRoleId(roleId);
                sysUserRoleDO.setUserId(userId);
                doList.add(sysUserRoleDO);
            }
            sysUserRoleService.saveBatch(doList);
        }
    }

}
