package io.laokou.auth.domain.sys.repository.mapper;

import io.laokou.common.vo.SysDeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
@Mapper
public interface SysDeptMapper {

    List<SysDeptVO> getDeptListByUserId(@Param("userId")Long userId);

    List<SysDeptVO> getDeptList();
}
