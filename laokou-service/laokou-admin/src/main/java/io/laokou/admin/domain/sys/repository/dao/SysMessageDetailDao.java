package io.laokou.admin.domain.sys.repository.dao;

import io.laokou.admin.domain.sys.entity.SysMessageDetailDO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysMessageDetailDao extends BaseDao<SysMessageDetailDO> {
}
