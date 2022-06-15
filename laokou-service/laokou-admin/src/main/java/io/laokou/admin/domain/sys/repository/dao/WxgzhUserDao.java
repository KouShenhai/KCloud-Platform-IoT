package io.laokou.admin.domain.sys.repository.dao;

import io.laokou.common.dao.BaseDao;
import io.laokou.admin.domain.sys.entity.WxgzhUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 */
@Repository
@Mapper
public interface WxgzhUserDao extends BaseDao<WxgzhUserDO> {
}
