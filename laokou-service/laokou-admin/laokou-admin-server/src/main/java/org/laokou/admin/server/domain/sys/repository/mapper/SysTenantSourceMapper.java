package org.laokou.admin.server.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.laokou.admin.server.domain.sys.entity.SysTenantSourceDO;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysTenantSourceMapper extends BaseMapper<SysTenantSourceDO> {
}
