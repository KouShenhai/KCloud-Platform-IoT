package io.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.laokou.admin.domain.sys.entity.SysDeptDO;
import io.laokou.admin.interfaces.qo.SysDeptQO;
import io.laokou.common.vo.SysDeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:15
 */
@Mapper
@Repository
public interface SysDeptMapper extends BaseMapper<SysDeptDO> {

    List<SysDeptVO> getDeptList(@Param("qo") SysDeptQO qo);

    void deleteDept(@Param("id") Long id);

    SysDeptVO getDept(@Param("id") Long id);

    List<Long> getDeptIdsByRoleId(@Param("roleId") Long roleId);

}
