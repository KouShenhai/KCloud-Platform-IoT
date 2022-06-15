package io.laokou.admin.domain.sys.repository.dao;

import io.laokou.common.dao.BaseDao;
import io.laokou.admin.domain.sys.entity.WxUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 */
@Repository
@Mapper
public interface WxUserDao extends BaseDao<WxUserDO> {
}
