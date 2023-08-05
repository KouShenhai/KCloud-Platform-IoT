package org.laokou.auth.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TenantMapper {

    List<String> getPermissionsByTenantId(@Param("tenantId")Long tenantId);

}
