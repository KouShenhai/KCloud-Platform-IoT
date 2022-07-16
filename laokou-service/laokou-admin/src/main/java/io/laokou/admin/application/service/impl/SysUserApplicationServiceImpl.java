package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.SysUserApplicationService;
import io.laokou.admin.domain.sys.entity.SysUserDO;
import io.laokou.admin.domain.sys.entity.SysUserRoleDO;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.domain.sys.repository.service.SysUserRoleService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.admin.infrastructure.common.password.PasswordUtil;
import io.laokou.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.SysUserDTO;
import io.laokou.admin.interfaces.qo.SysUserQO;
import io.laokou.common.vo.SysUserVO;
import io.laokou.common.enums.SuperAdminEnum;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.datasource.annotation.DataSource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
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
        UserDetail userDetail = sysUserService.getUserDetail(id, null);
        UserDetail userDetail2 = sysUserService.getUserDetail(userId, null);
        if (SuperAdminEnum.YES.ordinal() == userDetail.getSuperAdmin() && SuperAdminEnum.YES.ordinal() != userDetail2.getSuperAdmin()) {
            throw new CustomException("只有超级管理员才能修改");
        }
        int count = sysUserService.count(new LambdaQueryWrapper<SysUserDO>().eq(SysUserDO::getUsername, dto.getUsername()).ne(SysUserDO::getId,id));
        if (count > 0) {
            throw new CustomException("账号已存在，请重新填写");
        }
        dto.setEditor(userId);
        sysUserService.updateUser(dto);
        List<Long> roleIds = dto.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            //删除中间表
            sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRoleDO>().eq(SysUserRoleDO::getUserId, dto.getId()));
            saveOrUpdate(dto.getId(),roleIds);
        }
        return true;
    }

    @Override
    @DataSource("master")
    public Boolean insertUser(SysUserDTO dto, HttpServletRequest request) {
        SysUserDO sysUserDO = ConvertUtil.sourceToTarget(dto, SysUserDO.class);
        int count = sysUserService.count(new LambdaQueryWrapper<SysUserDO>().eq(SysUserDO::getUsername, sysUserDO.getUsername()));
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
        UserDetail userDetail = sysUserService.getUserDetail(id, null);
        UserDetail userDetail2 = sysUserService.getUserDetail(userId, null);
        if (SuperAdminEnum.YES.ordinal() == userDetail.getSuperAdmin() && SuperAdminEnum.YES.ordinal() != userDetail2.getSuperAdmin()) {
            throw new CustomException("只有超级管理员才能删除");
        }
        sysUserService.deleteUser(id);
        return true;
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
            sysUserRoleService.saveBatch(doList);
        }
        return false;
    }

}
