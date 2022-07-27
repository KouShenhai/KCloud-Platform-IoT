package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.SysRoleApplicationService;
import io.laokou.admin.domain.sys.entity.SysRoleDO;
import io.laokou.admin.domain.sys.entity.SysRoleDeptDO;
import io.laokou.admin.domain.sys.entity.SysRoleMenuDO;
import io.laokou.admin.domain.sys.repository.service.SysRoleDeptService;
import io.laokou.admin.domain.sys.repository.service.SysRoleMenuService;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.common.constant.Constant;
import io.laokou.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.SysRoleDTO;
import io.laokou.admin.interfaces.qo.SysRoleQO;
import io.laokou.common.user.UserDetail;
import io.laokou.common.vo.SysRoleVO;
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.datasource.annotation.DataFilter;
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
public class SysRoleApplicationServiceImpl implements SysRoleApplicationService {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    @DataSource("master")
    @DataFilter(tableAlias = "boot_sys_role")
    public IPage<SysRoleVO> queryRolePage(SysRoleQO qo) {
        IPage<SysRoleVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysRoleService.getRolePage(page,qo);
    }

    @Override
    @DataSource("master")
    public List<SysRoleVO> getRoleList(SysRoleQO qo) {
        return sysRoleService.getRoleList(qo);
    }

    @Override
    @DataSource("master")
    public SysRoleVO getRoleById(Long id) {
        return sysRoleService.getRoleById(id);
    }

    @Override
    @DataSource("master")
    public Boolean insertRole(SysRoleDTO dto, HttpServletRequest request) {
        SysRoleDO roleDO = ConvertUtil.sourceToTarget(dto, SysRoleDO.class);
        int count = sysRoleService.count(Wrappers.lambdaQuery(SysRoleDO.class).eq(SysRoleDO::getName, roleDO.getName()).eq(SysRoleDO::getDelFlag, Constant.NO));
        if (count > 0) {
            throw new CustomException("角色已存在，请重新填写");
        }
        final UserDetail userDetail = sysUserService.getUserDetail(SecurityUser.getUserId(request));
        roleDO.setCreator(userDetail.getId());
        roleDO.setDeptId(userDetail.getDeptId());
        sysRoleService.save(roleDO);
        List<Long> menuIds = dto.getMenuIds();
        saveOrUpdate(roleDO.getId(),menuIds,dto.getDeptIds());
        return true;
    }

    private Boolean saveOrUpdate(Long roleId,List<Long> menuIds,List<Long> deptIds) {
        boolean flag = false;
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<SysRoleMenuDO> roleMenuList = Lists.newArrayList();
            for (Long menuId : menuIds) {
                SysRoleMenuDO roleMenuDO = new SysRoleMenuDO();
                roleMenuDO.setMenuId(menuId);
                roleMenuDO.setRoleId(roleId);
                roleMenuList.add(roleMenuDO);
            }
            flag = sysRoleMenuService.saveBatch(roleMenuList);
        }
        if (CollectionUtils.isNotEmpty(deptIds)) {
            List<SysRoleDeptDO> roleDeptList = Lists.newArrayList();
            for (Long deptId : deptIds) {
                SysRoleDeptDO roleDeptDO = new SysRoleDeptDO();
                roleDeptDO.setDeptId(deptId);
                roleDeptDO.setRoleId(roleId);
                roleDeptList.add(roleDeptDO);
            }
            flag = flag && sysRoleDeptService.saveBatch(roleDeptList);
        }
        return flag;
    }

    @Override
    @DataSource("master")
    public Boolean updateRole(SysRoleDTO dto, HttpServletRequest request) {
        SysRoleDO roleDO = ConvertUtil.sourceToTarget(dto, SysRoleDO.class);
        int count = sysRoleService.count(Wrappers.lambdaQuery(SysRoleDO.class).eq(SysRoleDO::getName, roleDO.getName()).eq(SysRoleDO::getDelFlag, Constant.NO).ne(SysRoleDO::getId,roleDO.getId()));
        if (count > 0) {
            throw new CustomException("角色已存在，请重新填写");
        }
        Long userId = SecurityUser.getUserId(request);
        roleDO.setEditor(userId);
        sysRoleService.updateById(roleDO);
        //删除中间表
        sysRoleMenuService.remove(Wrappers.lambdaQuery(SysRoleMenuDO.class).eq(SysRoleMenuDO::getRoleId,dto.getId()));
        sysRoleDeptService.remove(Wrappers.lambdaQuery(SysRoleDeptDO.class).eq(SysRoleDeptDO::getRoleId,dto.getId()));
        saveOrUpdate(roleDO.getId(),dto.getMenuIds(),dto.getDeptIds());
        return true;
    }

    @Override
    @DataSource("master")
    public Boolean deleteRole(Long id) {
        sysRoleService.deleteRole(id);
        return true;
    }

}
