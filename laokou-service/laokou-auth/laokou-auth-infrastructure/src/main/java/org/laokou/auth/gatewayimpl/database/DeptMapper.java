package org.laokou.auth.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DeptMapper {

	List<Long> getDeptIdsByTenantId(@Param("tenantId") Long tenantId);

	List<Long> getDeptIdsByUserIdAndTenantId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

}
