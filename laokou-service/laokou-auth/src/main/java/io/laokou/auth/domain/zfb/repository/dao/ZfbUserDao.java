package io.laokou.auth.domain.zfb.repository.dao;
import io.laokou.auth.domain.zfb.entity.ZfbUserDO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 */
@Repository
@Mapper
public interface ZfbUserDao extends BaseDao<ZfbUserDO> {
}
