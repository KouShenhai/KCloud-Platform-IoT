package io.laokou.auth.domain.sys.repository.service.impl;
import io.laokou.auth.domain.sys.repository.dao.SysRoleDao;
import io.laokou.auth.domain.sys.repository.service.SysRoleService;
import io.laokou.common.vo.SysRoleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleDao sysRoleDao;

    @Override
    public List<Long> getRoleIds() {
        return sysRoleDao.getRoleIds();
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return sysRoleDao.getRoleIdsByUserId(userId);
    }

    @Override
    public List<SysRoleVO> getRoleListByUserId(Long userId) {
        return sysRoleDao.getRoleListByUserId(userId);
    }
}
