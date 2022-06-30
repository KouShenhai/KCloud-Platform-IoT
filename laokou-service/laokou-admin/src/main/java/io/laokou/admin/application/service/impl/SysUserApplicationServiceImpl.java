package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysUserApplicationService;
import io.laokou.admin.domain.sys.entity.SysUserDO;
import io.laokou.admin.domain.sys.entity.SysUserRoleDO;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.domain.sys.repository.service.SysUserRoleService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.admin.infrastructure.common.password.PasswordUtil;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.UserDTO;
import io.laokou.admin.interfaces.qo.UserQO;
import io.laokou.admin.interfaces.vo.UserVO;
import io.laokou.common.enums.SuperAdminEnum;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.ConvertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
public class SysUserApplicationServiceImpl implements SysUserApplicationService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Boolean updateUser(UserDTO dto, HttpServletRequest request) {
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
    public Boolean insertUser(UserDTO dto, HttpServletRequest request) {
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
    public IPage<UserVO> queryUserPage(UserQO qo) {
        IPage<UserVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysUserService.getUserPage(page,qo);
    }

    @Override
    public UserVO getUserById(Long id) {
        SysUserDO sysUserDO = sysUserService.getById(id);
        UserVO userVO = ConvertUtil.sourceToTarget(sysUserDO, UserVO.class);
        userVO.setRoleIds(sysRoleService.getRoleIdsByUserId(userVO.getId()));
        return userVO;
    }

    @Override
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
        return Boolean.FALSE;
    }

}
