package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.SysRoleApplicationService;
import io.laokou.admin.domain.sys.entity.SysRoleDO;
import io.laokou.admin.domain.sys.entity.SysRoleMenuDO;
import io.laokou.admin.domain.sys.repository.service.SysRoleMenuService;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.SysRoleDTO;
import io.laokou.admin.interfaces.qo.SysRoleQO;
import io.laokou.admin.interfaces.vo.SysRoleVO;
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.datasource.annotation.DataSource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysRoleApplicationServiceImpl implements SysRoleApplicationService {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    @DataSource("master")
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
        int count = sysRoleService.count(new LambdaQueryWrapper<SysRoleDO>().eq(SysRoleDO::getName, roleDO.getName()));
        if (count > 0) {
            throw new CustomException("角色已存在，请重新输入");
        }
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
        return false;
    }

    @Override
    @DataSource("master")
    public Boolean updateRole(SysRoleDTO dto, HttpServletRequest request) {
        SysRoleDO roleDO = ConvertUtil.sourceToTarget(dto, SysRoleDO.class);
        int count = sysRoleService.count(new LambdaQueryWrapper<SysRoleDO>().eq(SysRoleDO::getName, roleDO.getName()).ne(SysRoleDO::getId,roleDO.getId()));
        if (count > 0) {
            throw new CustomException("角色已存在，请重新输入");
        }
        Long userId = SecurityUser.getUserId(request);
        roleDO.setEditor(userId);
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
    @DataSource("master")
    public Boolean deleteRole(Long id) {
        sysRoleService.deleteRole(id);
        return true;
    }

}
