package io.laokou.admin.domain.sys.repository.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysRoleDO;
import io.laokou.admin.domain.sys.repository.dao.SysRoleDao;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.interfaces.dto.RoleDTO;
import io.laokou.admin.interfaces.qo.RoleQO;
import io.laokou.admin.interfaces.vo.RoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleDO> implements SysRoleService {

    @Override
    public List<Long> getRoleIds() {
        return this.baseMapper.getRoleIds();
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return this.baseMapper.getRoleIdsByUserId(userId);
    }

    @Override
    public List<String> getRoleNameList(Long userId) {
        return this.baseMapper.getRoleNameList(userId);
    }

    @Override
    public IPage<RoleVO> getRolePage(IPage<RoleVO> page, RoleQO qo) {
        return this.baseMapper.getRolePage(page, qo);
    }

    @Override
    public RoleVO getRoleById(Long id) {
        return this.baseMapper.getRoleById(id);
    }

    @Override
    public void deleteRole(Long id) {
        this.baseMapper.deleteRole(id);
    }

}
