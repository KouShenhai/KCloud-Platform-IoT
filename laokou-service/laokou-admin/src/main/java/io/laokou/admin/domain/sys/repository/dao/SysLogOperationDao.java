package io.laokou.admin.domain.sys.repository.dao;

import io.laokou.common.dao.BaseDao;
import io.laokou.admin.domain.sys.entity.SysLogOperationDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 操作日志 dao
 */
@Mapper
@Repository
public interface SysLogOperationDao extends BaseDao<SysLogOperationDO> {
	
}
