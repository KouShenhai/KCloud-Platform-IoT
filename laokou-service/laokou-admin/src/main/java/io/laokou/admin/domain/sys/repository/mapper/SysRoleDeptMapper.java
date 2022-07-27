package io.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.laokou.admin.domain.sys.entity.SysRoleDeptDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 上午 9:21
 */
@Repository
@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDeptDO> {
}
