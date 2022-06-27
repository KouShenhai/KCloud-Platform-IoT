package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysRoleApplicationService;
import io.laokou.admin.domain.sys.entity.SysRoleDO;
import io.laokou.admin.domain.sys.entity.SysRoleMenuDO;
import io.laokou.admin.domain.sys.repository.service.SysRoleMenuService;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.RoleDTO;
import io.laokou.admin.interfaces.qo.RoleQO;
import io.laokou.admin.interfaces.vo.RoleVO;
import io.laokou.common.utils.ConvertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
public class SysRoleApplicationServiceImpl implements SysRoleApplicationService {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public IPage<RoleVO> queryRolePage(RoleQO qo) {
        IPage<RoleVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysRoleService.getRolePage(page,qo);
    }

    @Override
    public List<RoleVO> getRoleList(RoleQO qo) {
        return sysRoleService.getRoleList(qo);
    }

    @Override
    public RoleVO getRoleById(Long id) {
        return sysRoleService.getRoleById(id);
    }

    @Override
    public Boolean insertRole(RoleDTO dto, HttpServletRequest request) {
        SysRoleDO roleDO = ConvertUtil.sourceToTarget(dto, SysRoleDO.class);
        roleDO.setCreator(SecurityUser.getUserId(request));
        sysRoleService.save(roleDO);
        List<Long> menuIds = dto.getMenuIds();
        if (CollectionUtils.isNotEmpty(menuIds)) {
            saveOrUpdate(roleDO.getId(),menuIds);
        }
        return true;
    }

    private Boolean saveOrUpdate(Long roleId,List<Long> menuIds) {
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<SysRoleMenuDO> roleMenuList = Lists.newArrayList();
            for (Long menuId : menuIds) {
                SysRoleMenuDO roleMenuDO = new SysRoleMenuDO();
                roleMenuDO.setMenuId(menuId);
                roleMenuDO.setRoleId(roleId);
                roleMenuList.add(roleMenuDO);
            }
            return sysRoleMenuService.saveBatch(roleMenuList);
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean updateRole(RoleDTO dto, HttpServletRequest request) {
        SysRoleDO roleDO = ConvertUtil.sourceToTarget(dto, SysRoleDO.class);
        roleDO.setEditor(SecurityUser.getUserId(request));
        sysRoleService.updateById(roleDO);
        List<Long> menuIds = dto.getMenuIds();
        if (CollectionUtils.isNotEmpty(menuIds)) {
            //删除中间表
            sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenuDO>().eq(SysRoleMenuDO::getRoleId,dto.getId()));
            saveOrUpdate(roleDO.getId(),dto.getMenuIds());
        }
        return true;
    }

    @Override
    public Boolean deleteRole(Long id) {
        sysRoleService.deleteRole(id);
        return true;
    }

}
