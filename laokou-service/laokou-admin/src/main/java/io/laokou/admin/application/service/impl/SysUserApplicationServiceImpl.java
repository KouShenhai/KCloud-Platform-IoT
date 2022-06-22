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
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.ConvertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
        dto.setEditor(SecurityUser.getUserId(request));
        sysUserService.updateUser(dto);
        List<Long> roleIds = dto.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            //删除中间表
            sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRoleDO>().eq(SysUserRoleDO::getUserId, dto.getId()));
            saveOrUpdate(dto.getId(),roleIds);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean insertUser(UserDTO dto, HttpServletRequest request) {
        SysUserDO sysUserDO = ConvertUtil.sourceToTarget(dto, SysUserDO.class);
        sysUserDO.setCreator(SecurityUser.getUserId(request));
        sysUserDO.setPassword(PasswordUtil.encode(dto.getPassword()));
        sysUserService.save(sysUserDO);
        List<Long> roleIds = dto.getRoleIds();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            saveOrUpdate(sysUserDO.getId(),roleIds);
        }
        return Boolean.TRUE;
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
    public Boolean deleteUser(Long id) {
        sysUserService.deleteUser(id);
        return Boolean.TRUE;
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
