package org.laokou.auth.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DeptMapper {

    List<Long> getDeptIdsByUserId(@Param("userId")Long userId,@Param("tenantId")Long tenantId);

    List<Long> getDeptIds(@Param("tenantId")Long tenantId);

}
