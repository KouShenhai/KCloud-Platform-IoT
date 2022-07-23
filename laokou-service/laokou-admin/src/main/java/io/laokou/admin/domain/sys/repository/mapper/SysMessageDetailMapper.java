package io.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.laokou.admin.domain.sys.entity.SysMessageDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysMessageDetailMapper extends BaseMapper<SysMessageDetailDO> {
}
