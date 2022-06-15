package io.laokou.admin.domain.sys.repository.dao;

import io.laokou.admin.domain.sys.entity.SysUserRoleDO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysUserRoleDao extends BaseDao<SysUserRoleDO> {
}
