package org.laokou.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.laokou.tenant.entity.SysPackageDO;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysPackageMapper extends BaseMapper<SysPackageDO> {

}
