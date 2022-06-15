package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysUserRoleDO;
import io.laokou.admin.domain.sys.repository.dao.SysUserRoleDao;
import io.laokou.admin.domain.sys.repository.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleDO> implements SysUserRoleService {
}
