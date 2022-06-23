package io.laokou.admin.domain.sys.repository.dao;

import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/6/23 0023 上午 10:22
 */
@Repository
@Mapper
public interface SysDictDao extends BaseDao<SysDictDO> {
}
